package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Category;
import com.bootcampproject.bootcamp_project.entity.CategoryMetaDataField;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Data
public class SellerListCategory {

    private Category category;
    private Map<CategoryMetaDataField, Set<String>> categoryMetaDataField;
    private Set<String> categoryMetaDataFieldValues;
}
