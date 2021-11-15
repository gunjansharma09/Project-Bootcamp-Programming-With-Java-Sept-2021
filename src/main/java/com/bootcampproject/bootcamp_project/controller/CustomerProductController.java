package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer")
public class CustomerProductController {

    @Autowired
    private ProductService productService;

    /*
      URI to fetch Details Of a Product given its id
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        try {
            Map<String, Object> data = productService.getProductDetailsById(id);
            return new ResponseEntity<Map<?, ?>>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/products/{catId}")
    public ResponseEntity<?> listAllProducts(
            @PathVariable(value = "catId") Long catId, @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy) {
        try {
            List<Map<Object, Object>> data = productService.getAllProductByCategoryId(pageNo, pageSize, sortBy, catId);
            return new ResponseEntity<List<?>>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
      URI to fetch Similar Product given its id
     */
    @GetMapping(value = "/view-similar-products")
    public ResponseEntity<?> viewSimilarProducts(
            @RequestParam Long productId,
            @RequestParam(defaultValue = "10") String page,
            @RequestParam(defaultValue = "0") String pageoff,
            @RequestParam(defaultValue = "id") String sortby,
            @RequestParam(defaultValue = "ASC") String order) {
        Integer pagesize = Integer.parseInt(page);
        Integer pageoffset = Integer.parseInt(pageoff);
        try {
            List<Map<Object, Object>> data = productService.viewSimilarProductsCustomer(productId, pageoffset, pagesize, sortby, order);
            return new ResponseEntity<List<?>>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
