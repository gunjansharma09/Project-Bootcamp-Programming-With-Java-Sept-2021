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
public class ProductUpdateDTO {

    @NotEmpty(message = "Please enter the name of the product")
    private String name;
    private String description = "No description".toUpperCase();
    private boolean isCancellable = false;
    private boolean isReturnable = false;
    @NotNull(message = "Please provide the product id to update")
    private Long productId;

}
