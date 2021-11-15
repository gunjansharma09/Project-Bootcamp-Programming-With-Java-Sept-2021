package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.entity.CategoryMetadataFieldValues;
import com.bootcampproject.bootcamp_project.service.AdminCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
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
    public ResponseEntity<?> addCategoryMetaDataField(@RequestParam @NotEmpty String name) {
        try {
            return new ResponseEntity<>(adminCategoryService.addCategoryMetadataField(name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //----------------------------------view all metadata field--------------------------------------------------------------------
    @GetMapping("/view/all/metadata/fields")
    public ResponseEntity<?> viewCategoryMetaDataField() {
        try {
            return new ResponseEntity<>(adminCategoryService.viewMetaDataFields(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //--------------------------------------add a category--------------------------------------------------------------------------
    @PostMapping("/add/category")
    public ResponseEntity<?> addCategory(@RequestParam String name, @RequestParam(required = false) Long parentId) {
        try {
            return new ResponseEntity<>(adminCategoryService.addCategory(name, parentId), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //-------------------------------------view all category-------------------------------------------------------------------------------
    @GetMapping("/view/all/category")
    public ResponseEntity<?> viewCategory(@RequestParam(defaultValue = "0") Integer pageNo,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          @RequestParam(defaultValue = "id") String sortBy) {
        try {
            return new ResponseEntity<>(adminCategoryService.listAllCategory(pageNo, pageSize, sortBy), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //---------------------------------------to update a category--------------------------------------------------------------------
    @PutMapping("/update/category")
    public ResponseEntity<?> updateCategory(@RequestParam @Valid Long id, @RequestParam String name) {
        try {
            return new ResponseEntity<>(adminCategoryService.updateCategory(id, name), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    //-------------------------------------to add new category metadata field for a category-------------------------------------------

    @PostMapping("/add/metadata/field/value")
    private ResponseEntity<?> addCategoryMetadataField(
            @RequestParam Long categoryId,
            @RequestParam Long metaDataFieldId,
            @RequestParam String values) {
        String[] arrValues = values.split(",");
        if (arrValues.length == 0)
            return new ResponseEntity<>("No values provided to set", HttpStatus.BAD_REQUEST);
        Set<String> valuesSet = Arrays.stream(arrValues).collect(Collectors.toSet());
        if (valuesSet.size() != arrValues.length)
            return new ResponseEntity<>("Values should be unique", HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(adminCategoryService.addCategoryMetaDataField(categoryId, metaDataFieldId, valuesSet), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    //---------------------------------------------------------to update values for existing metadata field ---------------------------------------------------

    @PutMapping("/update/metadata/field/value")
    private ResponseEntity<?> updateCategoryMetadataFieldValues(@RequestParam Long categoryId, @RequestParam long categoryMetadataFieldId, @RequestParam String values) {
        String[] arrValues = values.split(",");
        if (arrValues.length == 0)
            return new ResponseEntity<>("No valued provided to update", HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(adminCategoryService.updateCategoryMetadataFieldValues(categoryId, categoryMetadataFieldId, values), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
