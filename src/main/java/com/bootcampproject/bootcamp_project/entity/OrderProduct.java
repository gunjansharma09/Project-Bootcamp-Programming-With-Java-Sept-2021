package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Data
public class OrderProduct extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private int quantity;
    private double price;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Order_OrderProduct",
            joinColumns = @JoinColumn(name = "orderProduct_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Orders> orderId;


    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Order_ProductVariation",
            joinColumns = @JoinColumn(name = "orderProduct_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "productVariation_id",referencedColumnName = "id"))
    private List<ProductVariation> productVariations;

    @OneToOne(mappedBy = "orderProduct")
    private OrderStatus orderStatus;
}
