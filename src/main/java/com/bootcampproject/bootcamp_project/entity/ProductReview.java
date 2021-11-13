package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class ProductReview extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String review;
    private int rating;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "productId")
    private Product productId;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "customerUserId")
    private Customer customerId;
}
