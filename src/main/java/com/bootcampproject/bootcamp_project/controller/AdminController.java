package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.CustomerResponseDTO;
import com.bootcampproject.bootcamp_project.entity.Customer;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.service.AdminService;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;

    // API to find all the sellers
    @GetMapping("/list/sellers")
    public List<Seller> viewAllSeller(@RequestParam Integer pageSize, @RequestParam Integer pageOffset, @RequestParam String sortBy, @RequestParam String email) {
        return adminService.findListOfSellers(pageSize, pageOffset, sortBy, email);
    }

    // API to find all the customers
    @GetMapping("/list/customers")
    public ResponseEntity<?> viewAllCustomer(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageOffSet, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String email) {
        try {
            return ResponseEntity.ok(adminService.findListOfCustomer(pageSize, pageOffSet, sortBy, email));
        } catch (Exception e) {
            log.error("Exception occurred while fetching customer list,", e);
            return new ResponseEntity<>("Exception occurred while fetching customer list,", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PatchMapping("/customer/activate/{id}")
    public ResponseEntity<?> activeCustomerByAdmin(@PathVariable Long id) {

        return adminService.activateCustomerAccountByAdmin(id);
    }

    @PutMapping("/customer/deactivate/{id}")
    public String deactiveCustomerByAdmin(@PathVariable Long id) {
        return adminService.deactivateCustomerAccountByAdmin(id);
    }

    @PutMapping("/seller/activate/{id}")
    public String activeSellerByAdmin(@PathVariable Long id) {
        return adminService.activateSellerAccountByAdmin(id);
    }

    @PutMapping("/seller/deactivate/{id}")
    public String deactiveSellerByAdmin(@PathVariable Long id) {
        return adminService.deactivateSellerAccountByAdmin(id);
    }

}
