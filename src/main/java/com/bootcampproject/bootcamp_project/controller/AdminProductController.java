package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminProductController {

    @Autowired
    private ProductService productService;

    /*
      URI to activate a product
     */
    @PutMapping("/product/activate/{id}")
    public ResponseEntity<?> activateProduct(@PathVariable Long id) {
        try {
            String data = productService.activateDeActivateProduct(id, true);
            return new ResponseEntity<String>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /*
      URI to deactivate a product
     */
    @PutMapping("/product/deactivate/{id}")
    public ResponseEntity<?> deActivateProduct(@PathVariable Long id) {
        try {
            String data = productService.activateDeActivateProduct(id, false);
            return new ResponseEntity<String>(data, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /* throwing error
       URI to fetch Details Of a Product given its id
      */
    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProductById(@PathVariable(value = "id") Long id) {
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
}
