package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.CustomerProfileDto;
import com.bootcampproject.bootcamp_project.entity.Category;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/customer")
@PreAuthorize("hasAnyRole('ROLE_CUSTOMER')")
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;


    //--------------------------------------------to view profile-----------------------------------------------------------------------
    @GetMapping("/view/profile")
    public ResponseEntity<?> viewProfile() {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new NullPointerException("No email provided!");
        try {
            return new ResponseEntity<>(customerService.viewProfile(email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while displaying profile ", e);
            return new ResponseEntity<>("Exception occurred while displaying profile ", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //-----------------------------------to view address--------------------------------------------------------------------------------
    @GetMapping("/view/address")
    public ResponseEntity<?> viewAddress() {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new NullPointerException("No email provided!");
        try {
            return new ResponseEntity<>(customerService.viewAddress(email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while displaying address ", e);
            return new ResponseEntity<>("Exception occurred while displaying address ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //----------------------------------to update profile-------------------------------------------------------------------------------
    @PutMapping("/update/profile")
    public ResponseEntity<String> updateProfile(@RequestBody CustomerDto customerDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new NullPointerException("Email can not be null");
        try {
            return new ResponseEntity<>(customerService.updateCustomerProfile(customerDto, email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while updating profile! ", e);
            return new ResponseEntity<>("Exception occurred while updating profile !", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------------------------------to update password-----------------------------------------------------------------------------
    @PutMapping("/update/address")
    public ResponseEntity<String> updateAddress(@RequestBody AddressDto addressDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");
        try {
            return new ResponseEntity<>(customerService.updateAddress(addressDto, email), HttpStatus.OK);
        } catch (UserNotFoundException | AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while updating address ", e);
            return new ResponseEntity<>("Exception occurred while updating address ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-------------------------------------to update password----------------------------------------------------------------------------------
    @PutMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestHeader @NotNull String password, @RequestHeader @NotNull String confirmPassword) {

        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new NullPointerException("Email can not be null");
        try {
            return new ResponseEntity<>(customerService.updatePassword(password, confirmPassword, email), HttpStatus.OK);
        } catch (UserNotFoundException | NoPasswordFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PasswordNotMatchedException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error("Exception occurred while updating password ", e);
            return new ResponseEntity<>("Exception occurred while updating password ! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    //-------------------------------------to add password------------------------------------------------------------------------------
    @PostMapping("/add/password")
    public ResponseEntity<String> addAddress(@RequestBody AddressDto addressDto, @RequestParam Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new NullPointerException("Email can not be null!");
        try {
            return new ResponseEntity<>(customerService.addAddress(addressDto, email, id), HttpStatus.OK);
        } catch (EmailNotFoundException | UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while adding address ", e);
            return new ResponseEntity<>("Exception occurred while adding address ", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    //----------------------------to delete an address---------------------------------------------------------------
    @DeleteMapping("/delete/address")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            return new ResponseEntity<>(customerService.deleteAddress(id), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while deleting address ", e);
            return new ResponseEntity<>("Exception occurred while deleting address !", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    //-----------------------------------------------------------------------------------------------------------------------------------------
    //--------------------------------------------------------CATEGORY-------------------------------------------------------------------------------------------

    @GetMapping("/list/all/category")
    public ResponseEntity<?> listAllCategory(Long id) {
        try {
            return new ResponseEntity<>(customerService.categoryList(id), HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception occurred while listing all categories !", e);
            return new ResponseEntity<>("Exception occurred while listing all categories !", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
