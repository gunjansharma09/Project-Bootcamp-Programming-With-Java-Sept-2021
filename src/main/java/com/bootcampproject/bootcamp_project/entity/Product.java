package com.bootcampproject.bootcamp_project.entity;

import com.bootcampproject.bootcamp_project.enums.Status;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@Entity
@Data
public class Product extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String productName;
    private String productDescription;


    private boolean isCancellable;
    private boolean isReturnable;
    private String brand;


    private boolean isActive;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<ProductVariation> productVariation;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "categoryId")
    private Category categoryId;

//    @ManyToMany(cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "product_seller",
//            joinColumns = @JoinColumn(name = "product_id"),
//            inverseJoinColumns = @JoinColumn(name = "seller_id"))
//    private List<Seller> sellers;

    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    private Seller seller;



    @OneToOne(mappedBy = "productId")
    private ProductReview productReview;
}
