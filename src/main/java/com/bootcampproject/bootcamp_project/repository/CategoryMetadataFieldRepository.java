package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.CategoryMetaDataField;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryMetadataFieldRepository extends JpaRepository<CategoryMetaDataField, Long> {
    public Optional<CategoryMetaDataField> findByName(String name);

    public Optional<CategoryMetaDataField> findByNameIgnoreCase(String name);

}
