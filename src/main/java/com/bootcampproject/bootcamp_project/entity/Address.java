package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
public class Address extends AuditEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private Integer zipCode;
    private String label;
    private Boolean isDeleted=false;
//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "userId")
//    private User userId; // userId
}

