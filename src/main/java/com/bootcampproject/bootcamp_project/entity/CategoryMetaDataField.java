package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class CategoryMetaDataField extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @OneToMany(mappedBy = "categoryMetaDataField")
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues;
}