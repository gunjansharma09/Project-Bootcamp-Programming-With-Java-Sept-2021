package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.CustomerProfileDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;

    //--------------------------------------------to view profile-----------------------------------------------------------------------
    @GetMapping("/view/profile")
    public CustomerProfileDto viewProfile() {
        String email = SecurityContextUtil.findAuthenticatedUser();
        return customerService.viewProfile(email);
    }

    //-----------------------------------to view address--------------------------------------------------------------------------------
    @GetMapping("/view/address")
    public List<AddressDto> viewAddress() {
        String email = SecurityContextUtil.findAuthenticatedUser();
        return customerService.viewAddress(email);
    }

    //----------------------------------to update profile-------------------------------------------------------------------------------
    @PutMapping("/update/profile")
    public String updateProfile(@RequestBody CustomerDto customerDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return customerService.update(customerDto, email);
    }

    //------------------------------------to update password-----------------------------------------------------------------------------
    @PutMapping("/update/address")
    public String updateAddress(@RequestBody AddressDto addressDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return customerService.updateAddress(addressDto, email);
    }

    //-------------------------------------to update password----------------------------------------------------------------------------------
    @PutMapping("/update/password")
    public String updatePassword(@RequestHeader @NotNull String password, @RequestHeader @NotNull String confirmPassword) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        return customerService.updatePassword(password, confirmPassword, email);
    }

    //-------------------------------------to add password------------------------------------------------------------------------------
    @PostMapping("/add/password")
    public String addAddress(@RequestBody AddressDto addressDto, @RequestParam Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        return customerService.addAddress(addressDto, email, id);
    }

    //----------------------------to delete an address---------------------------------------------------------------
    @DeleteMapping("/delete/address")
    public String deleteAddress(@PathVariable Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        return customerService.deleteAddress(id);
    }


}
