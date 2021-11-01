package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class CategoryMetadataFieldValues extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String value;

//    @ManyToMany
//    @JoinColumn(name = "categoryMetaDataFieldId")
//    private CategoryMetaDataField categoryMetaDataFieldId;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category categoryId;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "catMetaDataField_catMetaDataFieldValues",
            joinColumns = @JoinColumn(name = "categoryMetadataFieldValues_id"),
            inverseJoinColumns = @JoinColumn(name = "categoryMetaDataField_id",referencedColumnName = "id"))
    private List<CategoryMetaDataField> categoryMetaDataFields;
}
