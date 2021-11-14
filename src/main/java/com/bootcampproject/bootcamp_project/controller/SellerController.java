package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.*;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.service.SellerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/seller")
@PreAuthorize("hasAnyRole('ROLE_SELLER')")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    //--------------------------------to view profile-----------------------------------------------------------------------------------------
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/view/profile")
    public ResponseEntity<?> viewProfile() {

        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new NullPointerException("No email provided");
        try {
            return new ResponseEntity<>(sellerService.viewProfile(email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while displaying profile", e);
            return new ResponseEntity<>("Exception occurred while displaying profile ", HttpStatus.BAD_REQUEST);
        }


    }

    //-------------------------------to update profile----------------------------------------------------------------------------------------
    @PutMapping("/update/profile")
    public ResponseEntity<String> updateProfile(@RequestBody SellerDto sellerDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email cannot be null");
        try {
            return new ResponseEntity<>(sellerService.updateProfile(sellerDto, email), HttpStatus.OK);
        } catch (CompanyAlreadyExistsException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred in updating profile", e);
            return new ResponseEntity<>("Exception occurred in updating profile ", HttpStatus.BAD_REQUEST);
        }
    }

    //----------------------------to update password------------------------------------------------------------------------------------------
    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestHeader @NotNull String password, @RequestHeader @NotNull String confirmPassword) {

        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");
        try {
            return new ResponseEntity<>(sellerService.updatePassword(email, password, confirmPassword), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NoPasswordFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PasswordNotMatchedException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error("Exception occurred while updating password!", e);
            return new ResponseEntity<>("Exception occurred while updating password! ", HttpStatus.BAD_REQUEST);
        }
    }

    /*@PutMapping("/update/password")
    public String updatePassword(@RequestBody SetPasswordDTO setPasswordDTO) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return sellerService.updatePassword(sellerDto, email);
    }*/
//---------------------------------to update address--------------------------------------------------------------------------------------
    @PutMapping("/update/address")
    public ResponseEntity<String> updateAddress(@RequestBody AddressDto addressDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");
        try {
            return new ResponseEntity<>(sellerService.updateAddress(addressDto, email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }


    }
}

