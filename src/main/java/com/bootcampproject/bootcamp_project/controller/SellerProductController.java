package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.ProductDTO;
import com.bootcampproject.bootcamp_project.dto.ProductUpdateDTO;
import com.bootcampproject.bootcamp_project.dto.ProductVariationDTO;
import com.bootcampproject.bootcamp_project.exceptions.CategoryNotFoundException;
import com.bootcampproject.bootcamp_project.exceptions.UserNotFoundException;
import com.bootcampproject.bootcamp_project.service.ProductService;
import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/seller")
@PreAuthorize("hasAnyRole('ROLE_SELLER')")
public class SellerProductController {

    @Autowired
    private ProductService productService;

    /*
      URI to add new Product.
     */
    @PostMapping(path = "/add/product")
    public ResponseEntity<String> addProduct(@Valid @RequestBody ProductDTO productDTO) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            String message = productService.addProduct(email, productDTO);
            return new ResponseEntity<String>(message, HttpStatus.CREATED);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (CategoryNotFoundException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
      URI to fetch Details Of a Product given its id
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            ProductDTO productDTO = productService.getProductByIdSeller(email, id);
            return new ResponseEntity<ProductDTO>(productDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
      URI to Fetch Details all Products
     */
    @GetMapping("/products")
    public ResponseEntity<?> listAllProduct(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            List<ProductDTO> productDTOS = productService.listAllProduct(email, pageNo, pageSize, sortBy);
            return new ResponseEntity<List<ProductDTO>>(productDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
      URI to Delete a Product
     */
    @DeleteMapping("/product/delete/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable @NotNull(message = "Please provide the product id to delete") Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            boolean isSuccess = productService.deleteProduct(email, id);
            if (isSuccess)
                return new ResponseEntity<String>("Product has been deleted successfully", HttpStatus.OK);
            else
                return new ResponseEntity<String>("Failed to delete product", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
      Update One Product
     */
    @PutMapping("/product/update/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable(value = "id") Long productId, @Valid @RequestBody
            ProductUpdateDTO productUpdateDTO) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            boolean isSuccess = productService.updateOneProduct(email, productId, productUpdateDTO);
            if (isSuccess)
                return new ResponseEntity<String>("Product has been updated successfully", HttpStatus.OK);
            else
                return new ResponseEntity<String>("Failed to update product", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /*
     URI to add variation of a product
    */
    @PostMapping(path = "/add/product-variation")
    public ResponseEntity<?> addProductVariation(@Valid @RequestBody ProductVariationDTO productVariationDTO) {
        try {
            boolean isSuccess = productService.addProductVariation(productVariationDTO);
            if (isSuccess)
                return new ResponseEntity<String>("Product variation added successfully", HttpStatus.OK);
            else
                return new ResponseEntity<String>("Failed to add product variation", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    /* image list not coming up
      URI to fetch details of one product Varion given its Id
     */
    @GetMapping("/product-variation/{id}")
    public ResponseEntity<?> getProductVariationById(
            @PathVariable(value = "id") Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            ProductVariationDTO productVariationDTO = productService.getProductVariationById(email, id);
            return new ResponseEntity<ProductVariationDTO>(productVariationDTO, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
