package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.CategoryMetaDataFieldValuesId;
import com.bootcampproject.bootcamp_project.entity.CategoryMetadataFieldValues;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryMetadataFieldValuesRepository extends JpaRepository<CategoryMetadataFieldValues, CategoryMetaDataFieldValuesId> {
}
