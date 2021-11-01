package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
public class Cart extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private boolean isWishlistItem;

    @OneToOne
    @JoinColumn(name = "customerUserId")
    private Customer customerId;


    private int quantity;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cart_productionVariation",
            joinColumns = @JoinColumn(name = "cart_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "productVariation_id",referencedColumnName = "id"))
    private List<ProductVariation> productVariations;
}
