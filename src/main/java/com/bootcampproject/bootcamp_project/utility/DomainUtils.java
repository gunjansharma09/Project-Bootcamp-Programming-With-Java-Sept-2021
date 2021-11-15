package com.bootcampproject.bootcamp_project.utility;

import com.bootcampproject.bootcamp_project.dto.*;
import com.bootcampproject.bootcamp_project.entity.*;
import com.bootcampproject.bootcamp_project.exceptions.InvalidGSTNumberException;
import com.bootcampproject.bootcamp_project.exceptions.InvalidPasswordException;
import com.bootcampproject.bootcamp_project.validator.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DomainUtils {

    //To create a user
    public static User toUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        user.setCreatedBy(getCreatedBy());
        if (!Validator.isValidatedPassword(userDto.getPassword())) {
            throw new InvalidPasswordException("Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number");
        }
        if (passwordEncoder != null)
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        else
            user.setPassword(userDto.getPassword());
        return user;
    }

    //To set address
    public static Address toAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setZipCode(addressDto.getZipCode());
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setState(addressDto.getState());
        address.setCreatedBy(getCreatedBy());
        return address;
    }

    //To set seller's company's details
    public static Seller toSeller(SellerDto sellerDto) {
        Seller seller = new Seller();
        seller.setCompanyContact(sellerDto.getCompanyContact());
        seller.setCompanyName(sellerDto.getCompanyName());
        seller.setGst(sellerDto.getGst());
        seller.setCreatedBy(getCreatedBy());

        if (!Validator.isValidatedGST(sellerDto.getGst())) {
            throw new InvalidGSTNumberException("Please provide a valid GST number");
        }
        return seller;
    }

    public static Product toProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductName(productDTO.getName());
        product.setProductReview(product.getProductReview());
        product.setProductDescription(productDTO.getDescription());
        product.setActive(product.isActive());
        product.setCancellable(productDTO.isCancellable());
        product.setBrand(productDTO.getBrand());
        product.setCreatedBy(getCreatedBy());
        return product;
    }

    public static void updateProduct(ProductUpdateDTO productUpdateDTO, Product product) {
        product.setProductName(productUpdateDTO.getName());
        product.setProductDescription(productUpdateDTO.getDescription());
        product.setCancellable(productUpdateDTO.isCancellable());
        product.setReturnable(productUpdateDTO.isReturnable());
    }

    public static ProductVariation toProductVariation(ProductVariationDTO productVariationDTO) {
        ProductVariation productVariation = new ProductVariation();
        productVariation.setActive(productVariationDTO.isActive());
        productVariation.setQuantityAvailable(productVariationDTO.getQuantityAvailable());
        productVariation.setPrimaryImageName(productVariationDTO.getPrimaryImageName());
        productVariation.setPrice(productVariationDTO.getPrice());
        return productVariation;
    }

    public static String getCreatedBy() {
        String createdBy = SecurityContextUtil.findAuthenticatedUser();
        createdBy = (createdBy == null) ? "system" : createdBy; // bootstrap jb chalta h to vo application run hone k baad chalta h.. usme koi b user loggedin ni hota..

        return createdBy;
    }

}
