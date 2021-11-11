package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.*;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.service.SellerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;
//--------------------------------to view profile-----------------------------------------------------------------------------------------
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/view/profile")
    public SellerProfileDto viewProfile() {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return sellerService.viewProfile(email);
    }
//-------------------------------to update profile----------------------------------------------------------------------------------------
    @PutMapping("/update/profile")
    public String updateProfile(@RequestBody SellerDto sellerDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return sellerService.update(sellerDto, email);
    }
//----------------------------to update password------------------------------------------------------------------------------------------
    @PutMapping("/update/password")
    public String updatePassword(@RequestHeader @NotNull String password, @RequestHeader @NotNull String confirmPassword) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return sellerService.updatePassword(email, password, confirmPassword);
    }

    /*@PutMapping("/update/password")
    public String updatePassword(@RequestBody SetPasswordDTO setPasswordDTO) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return sellerService.updatePassword(sellerDto, email);
    }*/
//---------------------------------to update address--------------------------------------------------------------------------------------
    @PutMapping("/update/address")
    public String updateAddress(@RequestBody AddressDto addressDto)
    {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return sellerService.updateAddress(addressDto,email);
    }
}

