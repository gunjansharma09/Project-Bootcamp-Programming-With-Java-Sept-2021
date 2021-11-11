package com.bootcampproject.bootcamp_project.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GeneratorType;

import javax.annotation.Generated;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Customer extends AuditEntity implements Serializable {
    private String contact;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "customerId")
    private List<Orders> order;

    @OneToMany(mappedBy = "customerId")
    private List<ProductReview> productReview;

    @OneToOne(mappedBy = "customerId")
    private Cart cart;

    private String accountActivateToken;

    private Long accountActivateTokenGeneratedAt;

}




