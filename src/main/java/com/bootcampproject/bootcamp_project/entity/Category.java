package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Entity
@Data
public class Category extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parentCategoryId")
    private Category category;

    @OneToOne(mappedBy = "categoryId")
    private Product product;

    @OneToMany(mappedBy = "categoryId")
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues;
}
