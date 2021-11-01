package com.bootcampproject.bootcamp_project.entity;

import com.bootcampproject.bootcamp_project.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
public class ProductVariation extends AuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private double price;
    private int quantityAvailable;
    private String primaryImageName;


    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "productId")
    private Product product;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Order_ProductVariation",
            joinColumns = @JoinColumn(name = "productVariation_id"),
            inverseJoinColumns = @JoinColumn(name = "orderProduct_id"))
    private List<OrderProduct> orderProducts;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "cart_productionVariation",
            joinColumns = @JoinColumn(name = "productVariation_id"),
            inverseJoinColumns = @JoinColumn(name = "cart_id"))
    private List<Cart> cart;
}
