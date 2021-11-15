package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.ProductDTO;
import com.bootcampproject.bootcamp_project.dto.ProductUpdateDTO;
import com.bootcampproject.bootcamp_project.dto.ProductVariationDTO;
import com.bootcampproject.bootcamp_project.entity.*;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.repository.*;
import com.bootcampproject.bootcamp_project.utility.DomainUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductVariationRepository productVariationRepository;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private EmailService emailSenderService;

  /*@Autowired
  private ImageUploaderService imageUploaderService;*/


    //-----------------------------------------SELLER API-----------------------------------------------
  /*
    Adds new product to the product List
   */
    public String addProduct(String email, ProductDTO productDTO) {

        boolean exists = categoryRepository.existsById(productDTO.getCategoryId());
        if (!exists) {
            throw new CategoryNotFoundException("Category does not exists");
        }

        Category category = categoryRepository.findById(productDTO.getCategoryId()).get();

        Seller seller = checkAndReturnSeller(email);

        List<Product> productList = productRepository.findAllBySellerUserId(seller.getId());
        if (productList != null) {
            productList.forEach(e -> {
                if (e.getBrand().equalsIgnoreCase(productDTO.getBrand())
                        && e.getCategoryId().getId() == productDTO.getCategoryId()
                        && e.getBrand().equalsIgnoreCase(productDTO.getBrand())) {
                    throw new ProductAlreadyExistsException("Product Alread Exists");
                }
            });
        }

        Product product = DomainUtils.toProduct(productDTO);
        product.setCategoryId(category);
        product.setSeller(seller);
        productRepository.save(product);

        return "Congratulations, Product added successfully!";
    }

    private Seller checkAndReturnSeller(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("Seller not found with this email id:" + email);

        Seller seller = optionalUser.get().getSeller();
        if (seller == null)
            throw new UserNotFoundException("Seller not found with this email id:" + email);
        return seller;
    }

    /*
      Get Details Of One Product by Id
     */
    public ProductDTO getProductByIdSeller(String email, Long productId) {
        Seller seller = checkAndReturnSeller(email);
        Optional<Product> optionalProduct = productRepository.findById(productId);

        //if product does not exists
        if (!optionalProduct.isPresent()) {
            throw new ProductNotFoundException("Product not found with id:" + productId);
        }

        Product product = optionalProduct.get();
        //if product is not added by the seller
        if (product.getSeller().getId() != seller.getId()) {
            throw new ProductNotFoundException("Invalid Product Id");
        }
        return new ProductDTO(product);
    }


    /* @Untested
      Fetch Details Of All Products
     */
    public List<ProductDTO> listAllProduct(String email, Integer pageNo, Integer pageSize, String sortBy) {
        Seller seller = checkAndReturnSeller(email);
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        List<Product> productList = productRepository.listAllProduct(paging, seller.getId());
        if (productList == null)
            return new ArrayList<>();
        return productList.stream().map(ProductDTO::new).collect(Collectors.toList());
    }

    /*@Untested
      METHOD to Delete One Product
     */
    public boolean deleteProduct(String email, Long id) {
        Seller seller = checkAndReturnSeller(email);
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ProductNotFoundException("Invalid product Id");
        }
        Product product = optionalProduct.get();

        if (product.getSeller().getId() != seller.getId()) {
            throw new ProductNotFoundException("No such product available");
        }
        productRepository.delete(product);
        return true;
    }

    /*@Untested
      METHOD to Update a Product
     */
    public boolean updateOneProduct(String email, Long id, ProductUpdateDTO productUpdateDTO) {
        Seller seller = checkAndReturnSeller(email);
        Optional<Product> optionalProduct = productRepository.findById(id);

        if (!optionalProduct.isPresent()) {      //product ID exist or not
            throw new ProductNotFoundException("Invalid Product Id");
        }
        Product product = optionalProduct.get();
        if (product.getSeller().getId() != seller.getId()) {
            throw new ProductNotFoundException("No such product exists");
        }
        DomainUtils.updateProduct(productUpdateDTO, product);
        productRepository.save(product);
        return true;
    }


    /*
      Adds new product Variation
     */
    public boolean addProductVariation(ProductVariationDTO productVariationDTO) {
        Long productId = productVariationDTO.getProductId();
        Optional<Product> optionalProduct = productRepository.findById(productId);

        if (!optionalProduct.isPresent())
            throw new ProductNotFoundException("Enter a valid Product Id");

        ProductVariation productVariation = DomainUtils.toProductVariation(productVariationDTO);
        productVariation.setProduct(optionalProduct.get());
        productVariationRepository.save(productVariation);
        return true;
    }

    /*
      Method to get details of one product variation by id
     */
    public ProductVariationDTO getProductVariationById(String email, Long productVationId) {

        Long sellerId = checkAndReturnSeller(email).getId();
        Optional<ProductVariation> optionalProductVariation = productVariationRepository.findById(productVationId);

        if (!optionalProductVariation.isPresent()) {
            throw new ProductVariationNotFoundException("Invalid Product Variation Id");
        }
        ProductVariation productVariation = optionalProductVariation.get();

        if (!productVariation.getProduct().getSeller().getId().equals(sellerId)) {
            throw new ProductVariationNotFoundException("Invalid Product Variation Id");
        }
        ProductVariationDTO productVariationDTO = new ProductVariationDTO(productVariation);
        productVariationDTO.setProductId(productVariation.getProduct().getId());
        return productVariationDTO;
    }


//-----------------------------------------CUSTOMER API---------------------------------------------


    public Map<String, Object> getProductDetailsById(Long productId) {
        boolean exists = productRepository.existsById(productId);
        if (!exists) {
            throw new ProductNotFoundException("Invalid product Id");
        }

        Product product = productRepository.findById(productId).get();
        if (!product.isActive()) {
            throw new ProductNotFoundException("Product is not activated!");
        }

        List<ProductVariation> productVariationList = productVariationRepository.findByProductId(product);
        if (productVariationList == null) {
            productVariationList = new ArrayList<>();
        }

        List<ProductVariationDTO> productVariationDTOList = productVariationList
                .stream()
                .map(ProductVariationDTO::new)
                .collect(Collectors.toList());

        Map<String, Object> responseSet = new HashMap<>();
        responseSet.put("Product details :", new ProductDTO(product));
        responseSet.put("Variations available", productVariationDTOList);
        return responseSet;
    }

    /*
      API to view all products
     */
    public List<Map<Object, Object>> getAllProductByCategoryId(Integer pageNo, Integer pageSize, String sortBy, Long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent())
            throw new CategoryNotFoundException("Invalid Category Id");

        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        return productRepository.listAllProductCustomer(paging, categoryId);
    }

    public List<Map<Object, Object>> viewSimilarProductsCustomer(Long productId, Integer pageoffset, Integer pagesize, String sortBy, String order) {
        Pageable pageable;
        List<Map<Object, Object>> responseList;

        Optional<Long> optional = productRepository.findCategoryidIsActive(productId);
        if (!optional.isPresent()) {
            throw new ProductNotFoundException("Invalid Product Id..");
        }

        if (order.equals("DESC")) {
            pageable = PageRequest
                    .of(pageoffset, pagesize, Sort.by(new Sort.Order(Sort.Direction.DESC, sortBy)));
        } else {
            pageable = PageRequest
                    .of(pageoffset, pagesize, Sort.by(new Sort.Order(Sort.Direction.ASC, sortBy)));
        }
        Long categoryId = productRepository.findById(productId).get().getCategoryId().getId();
        return productRepository.findAllProductsForCustomer(categoryId, pageable);
    }

//-----------------------------------------ADMIN API------------------------------------------------

    /*
      Method to Activate A Product
     */
    public String activateDeActivateProduct(Long id, boolean activate) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ProductNotFoundException("Product Id Not Found!");
        }

        Product product = optionalProduct.get();
        Seller seller = product.getSeller();
        if (activate) {
            if (!product.isActive()) {
                product.setActive(true);
                productRepository.save(product);
                emailSenderService.sendEmailAsync(seller.getUser().getEmail(), "Congratulations!", "Your product " + id + " has been activated by the Admin");
                return "Product has been activated successfully!";
            } else
                return "Product is already activated!";
        } else {
            if (product.isActive()) {
                product.setActive(false);
                productRepository.save(product);
                emailSenderService.sendEmailAsync(seller.getUser().getEmail(), "Your Product updates", "Your product " + id + " has been deactivated by the Admin");
                return "Product has been deactivated successfully!";
            } else
                return "Product is already deactivated!";
        }
    }
}
