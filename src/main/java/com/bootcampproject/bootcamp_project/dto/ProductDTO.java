package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

    @NotEmpty(message = "Please enter the name of the product")
    private String name;
    private String description = "No description".toUpperCase();
    @NotNull(message = "category id not entered")
    private Long categoryId;
    private boolean isCancellable = false;
    private boolean isReturnable = false;
    @NotEmpty(message = "Brand name can not be empty")
    private String brand;

    public ProductDTO(Product product) {
        this.name = product.getProductName();
        this.description = product.getProductDescription();
        this.categoryId = product.getCategoryId().getId();
        this.isCancellable = product.isCancellable();
        this.isReturnable = product.isReturnable();
        this.brand = product.getBrand();
    }
}
