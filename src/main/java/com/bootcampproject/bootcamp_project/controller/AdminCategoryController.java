package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.entity.Category;
import com.bootcampproject.bootcamp_project.entity.CategoryMetaDataField;
import com.bootcampproject.bootcamp_project.entity.CategoryMetadataFieldValues;
import com.bootcampproject.bootcamp_project.exceptions.DuplicateValueException;
import com.bootcampproject.bootcamp_project.exceptions.NoValueExceptionException;
import com.bootcampproject.bootcamp_project.service.AdminCategoryService;
import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminCategoryController {
    @Autowired
    private AdminCategoryService adminCategoryService;

    //------------------------------------add a metadata field------------------------------------------------------------------------
    @PostMapping("/add/metadata/field")
    public CategoryMetaDataField addCategoryMetaDataField(@RequestParam @NotNull String name) {
        return adminCategoryService.addCategoryMetadataField(name);
    }

    //----------------------------------view all metadata field--------------------------------------------------------------------
    @GetMapping("/view/all/metadata/fields")
    public List<CategoryMetaDataField> viewCategoryMetaDataField() {

        return adminCategoryService.viewMetaDataFields();
    }

    //--------------------------------------add a category--------------------------------------------------------------------------
    @PostMapping("/add/category")
    public Category addCategory(@RequestParam String name, @RequestParam(required = false) Long parentId) {

        return adminCategoryService.addCategory(name, parentId);
    }

    //-------------------------------------view a category-------------------------------------------------------------------------------
    @GetMapping("/view/all/category")
    public List<Category> viewCategory(@RequestParam(defaultValue = "0") Integer pageNo,
                                       @RequestParam(defaultValue = "10") Integer pageSize,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        return adminCategoryService.listAllCategory(pageNo, pageSize, sortBy);
    }

    //---------------------------------------to update a category--------------------------------------------------------------------
    @PutMapping("/update/category")
    public Category updateCategory(@RequestParam @Valid Long id, @RequestParam String name) {

        return adminCategoryService.updateCategory(id, name);
    }

    //-------------------------------------to add new category metadata field for a category-------------------------------------------

    @PostMapping("/add/metadata/field/value")
    private CategoryMetadataFieldValues addCategoryMetadataField(
            @RequestParam Long categoryId,
            @RequestParam Long metaDataFieldId,
            @RequestParam String values) {
        String[] arrValues = values.split(",");
        if (arrValues.length == 0)
            throw new NoValueExceptionException("No values provided to set");
        Set<String> valuesSet = Arrays.stream(arrValues).collect(Collectors.toSet());
        if (valuesSet.size() != arrValues.length)
            throw new DuplicateValueException("Values should be unique");
        return adminCategoryService.addCategoryMetaDataField(categoryId, metaDataFieldId, valuesSet);
    }


    //---------------------------------------------------------to update values for existing metadata field ---------------------------------------------------

    @PutMapping("/update/metadata/field/value")
    private String updateCategoryMetadataFieldValues(@RequestParam Long categoryId, @RequestParam long categoryMetadataFieldId, @RequestParam String values) {
        String[] arrValues = values.split(",");
        if (arrValues.length == 0)
            throw new NoValueExceptionException("No valued provided to update");
        return adminCategoryService.updateCategoryMetadataFieldValues(categoryId, categoryMetadataFieldId, values);
    }
}
