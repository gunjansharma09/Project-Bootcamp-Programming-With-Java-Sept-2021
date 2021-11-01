package com.bootcampproject.bootcamp_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AuditEntity {
    private String contact;
    @Id
    private Long id;
    @MapsId
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "customerId")
    private List<Orders> order;

    @OneToMany(mappedBy = "customerId")
    private List<ProductReview> productReview;

    @OneToOne(mappedBy = "customerId")
    private Cart cart;

}




