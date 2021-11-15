package com.bootcampproject.bootcamp_project.dto;


import com.bootcampproject.bootcamp_project.entity.ProductVariation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariationDTO {

    @NotNull(message = "Product Id is must")
    private Long productId;
    @NotNull(message = "quantity is must")
    private Integer quantityAvailable;
    @NotNull(message = "price is must")
    private double price;
    private boolean isActive;
    private String primaryImageName;

    public ProductVariationDTO(ProductVariation productVariation) {
        this.quantityAvailable = productVariation.getQuantityAvailable();
        this.price = productVariation.getPrice();
        this.isActive = productVariation.isActive();
        this.primaryImageName = productVariation.getPrimaryImageName();
    }
}
