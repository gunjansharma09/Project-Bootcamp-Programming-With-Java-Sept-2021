package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.entity.Category;
import com.bootcampproject.bootcamp_project.entity.CategoryMetaDataField;
import com.bootcampproject.bootcamp_project.entity.CategoryMetaDataFieldValuesId;
import com.bootcampproject.bootcamp_project.entity.CategoryMetadataFieldValues;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.repository.CategoryMetadataFieldRepository;
import com.bootcampproject.bootcamp_project.repository.CategoryMetadataFieldValuesRepository;
import com.bootcampproject.bootcamp_project.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AdminCategoryService {
    @Autowired
    private CategoryMetadataFieldRepository categoryMetadataFieldRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMetadataFieldValuesRepository categoryMetadataFieldValuesRepository;

    //--------------------------------to add metadata field-------------------------------------------------------------------
    public CategoryMetaDataField addCategoryMetadataField(String name) {
        if (name == null && name.equals(""))
            throw new NullPointerException("Name cannot be null or empty");

        Optional<CategoryMetaDataField> optionalCategoryMetaDataField = categoryMetadataFieldRepository.findByName(name);
        if (optionalCategoryMetaDataField.isPresent())
            throw new MetadataFieldAlreadyExistsException("Category metadata field already exists in database!");

        CategoryMetaDataField categoryMetaDataField = new CategoryMetaDataField();
        categoryMetaDataField.setName(name);
        return categoryMetadataFieldRepository.save(categoryMetaDataField);
    }

    //------------------------------to view all metadata fields-------------------------------------------------------

    public List<CategoryMetaDataField> viewMetaDataFields() {
        List<CategoryMetaDataField> categoryMetaDataField = categoryMetadataFieldRepository.findAll();
        return categoryMetaDataField;
    }

    /*
    Method to list All Category
   */
    public List<Category> listAllCategory(Integer pageNo, Integer pageSize,
                                          String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy).ascending());
        return categoryRepository.findAll(paging).toList();
    }

    //------------------------------to add a category-------------------------------------------------------------------

    public Category addCategory(String name, Long parentId) {
        Optional<Category> optionalCategoryName = categoryRepository.findByName(name);
        if (optionalCategoryName.isPresent())
            throw new CategoryAlreadyExistsException("Category is already exists with name " + name);

        Category newCategory = new Category();
        newCategory.setName(name);

        if (parentId != null) {
            Optional<Category> optionalParentId = categoryRepository.findById(parentId);
            if (!optionalParentId.isPresent())
                throw new CategoryNotFoundException("Parent id not found with name " + parentId);

            Category parentCategoryId = optionalParentId.get();
            newCategory.setParentCategory(parentCategoryId);
        }
        return categoryRepository.save(newCategory);
    }

    //-----------------------------------to view a category--------------------------------------------------------------------

//    public Category viewCategory(Long id) {
//        Optional<Category> optionalCategory = categoryRepository.findById(id);
//        if (optionalCategory.isPresent()) {
//            Category category = optionalCategory.get();
//            List<Category> subCategory = categoryRepository.findAll();
//        }
//
//        return
//    }


    //----------------------------to view all categories------------------------------------------------------------------------

//    public List<Category> viewAllCategory(Long id) {
//
//    }

    //------------------------------to update a category---------------------------------------------------------------------------
    public Category updateCategory(Long id, String name) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            throw new CategoryNotFoundException("No Such Category Exists");
        }
        Category savedCategory = optionalCategory.get();

        Optional<Category> oldCategoryName = categoryRepository.findByName(name);
        if (oldCategoryName.isPresent())
            throw new CategoryAlreadyExistsException("A category with this name already exists!");

        savedCategory.setName(name);
        return categoryRepository.save(savedCategory);
    }

    //-----------------------------------to add a new category metadata field value for a category----------------------------------------
    public CategoryMetadataFieldValues addCategoryMetaDataField(Long categoryId, Long metaDataFieldId, Set<String> values) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent())
            throw new CategoryNotFoundException("No category found with this id: " + categoryId);

        Optional<CategoryMetaDataField> optionalCategoryMetaDataField = categoryMetadataFieldRepository.findById(metaDataFieldId);
        if (!optionalCategoryMetaDataField.isPresent())
            throw new MetadataFieldNotFoundException("No Metadata found with this id: " + metaDataFieldId);

        Category category = optionalCategory.get();
        CategoryMetaDataField categoryMetaDataField = optionalCategoryMetaDataField.get();

        CategoryMetadataFieldValues categoryMetadataFieldValues = new CategoryMetadataFieldValues();
        categoryMetadataFieldValues.setCategory(category);
        categoryMetadataFieldValues.setCategoryMetaDataField(categoryMetaDataField);
        categoryMetadataFieldValues.setValue(values.stream().collect(Collectors.joining(",")));

        return categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);

    }

    //--------------------------------to update values in existing metadata field in a category-----------------------------------------

    public String updateCategoryMetadataFieldValues(Long categoryId, Long categoryMetaDataFieldId, String values) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        if (!optionalCategory.isPresent())
            throw new CategoryNotFoundException("Category not found with id " + categoryId);
        Optional<CategoryMetaDataField> optionalCategoryMetaDataField = categoryMetadataFieldRepository.findById(categoryMetaDataFieldId);
        if (!optionalCategoryMetaDataField.isPresent())
            throw new CategoryNotFoundException("Category Metadata Field values does not exists with id " + categoryMetaDataFieldId);

        Category category1 = optionalCategory.get();
        CategoryMetaDataField categoryMetaDataField1 = optionalCategoryMetaDataField.get();
        CategoryMetaDataFieldValuesId categoryMetaDataFieldValuesId = new CategoryMetaDataFieldValuesId();
        Optional<CategoryMetadataFieldValues> optionalCategoryMetadataFieldValues = categoryMetadataFieldValuesRepository.findById(categoryMetaDataFieldValuesId);
        if (optionalCategoryMetadataFieldValues.isPresent()) {
            CategoryMetadataFieldValues categoryMetadataFieldValues = optionalCategoryMetadataFieldValues.get();
            categoryMetadataFieldValues.setValue(values);
            categoryMetadataFieldValuesRepository.save(categoryMetadataFieldValues);
        } else {
            throw new MetaDataFieldValueDoesNotExists("No existing value found for this category id " + categoryId + " and MetaData Field id " + categoryMetaDataFieldId);
        }
        return "Success";

    }


}
