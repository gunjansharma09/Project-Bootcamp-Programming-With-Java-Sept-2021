package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
@Entity
@Data
public class Address extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    private Integer zipCode;
    private String label;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId")
    private User userId; // userId
}

