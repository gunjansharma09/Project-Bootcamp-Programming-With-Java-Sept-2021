package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.entity.Customer;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.service.AdminService;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
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
    public List<Customer> viewAllCustomer(@RequestParam Integer pageSize, @RequestParam Integer pageOffSet, @RequestParam String sortBy, @RequestParam String email) {
        return adminService.findListOfCustomer(pageSize, pageOffSet, sortBy, email);
    }

    @PatchMapping("/customer/activate/{id}")
    public String activeCustomerByAdmin(@PathVariable Long id) {
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
