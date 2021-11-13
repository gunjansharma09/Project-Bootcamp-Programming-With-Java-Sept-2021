package com.bootcampproject.bootcamp_project.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@IdClass(CategoryMetaDataFieldValuesId.class)
public class CategoryMetadataFieldValues extends AuditEntity {

    @Id
    @ManyToOne
    @JoinColumn(name = "CategoryMetaDataFieldId")
    private CategoryMetaDataField categoryMetaDataField;

    @Id
    @ManyToOne
    @JoinColumn(name = "CategoryId")
    private Category category;

    private String value;

}





