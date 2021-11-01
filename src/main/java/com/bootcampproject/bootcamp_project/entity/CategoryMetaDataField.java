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

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "catMetaDataField_catMetaDataFieldValues",
            joinColumns = @JoinColumn(name = "categoryMetaDataField_id",referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "categoryMetadataFieldValues_id"))
    private List<CategoryMetadataFieldValues> categoryMetadataFieldValues;
}
