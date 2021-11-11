package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.SellerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@RestController
@RequestMapping
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    @PostMapping("/register/customer")
    public String registerCustomer(@Valid @RequestBody CustomerDto customerDto) {
        if (customerService.saveCustomer(customerDto))
            return "success";
        else return "fail";
    }

    @PostMapping("/register/seller")
    public String registerSeller(@Valid @RequestBody SellerDto sellerDto) {
            if (sellerService.saveSeller(sellerDto))
                return "success";
            else return "fail";
    }

    @PostMapping("/forgot/password/{email}")
    public String forgotPassword(@PathVariable String email) {
        return userService.forgotPassword(email);
    }

    @PutMapping("set/password")
    public String updatePassword(@RequestParam @NotBlank String token, @RequestParam @NotBlank @Size(min = 8) String password) {
        return userService.updatePassword(token, password);
    }

    @GetMapping("/customer/activate/{token}")
    public String activateCustomer(@PathVariable String token) {
        return customerService.activateAccount(token);
    }


//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/createAccount")
//    public String save(@Valid @RequestBody UserDto userDto) {
//        if (userService.save(userDto, null) == null) {
//            return "Fail";
//        }
//        return "success";
//    }
//
//    @DeleteMapping("/deleteAccount")
//    public String delete(@RequestParam Long id) {
//        userService.delete(id);
//        return "success";
//
//    }
//
//    @PutMapping("/updateAccount")
//    public String update(@Valid @RequestBody UserDto userDto) {
//        userService.save(userDto, null);
//        return "success";
//    }


}
