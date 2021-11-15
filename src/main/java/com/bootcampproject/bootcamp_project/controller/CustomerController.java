package com.bootcampproject.bootcamp_project.controller;
//to do -- null pointer remove
// -- update profile me seller jaisa
//--email unique, phone no , password valid, confirm match --- customer create me hoga ye.. cutomer dto me
//--update password
//--request param required false
//--

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.CustomerProfileDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.entity.Category;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import com.bootcampproject.bootcamp_project.validator.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
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
            throw new EmailNotFoundException("No email provided!");
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
            throw new EmailNotFoundException("No email provided!");
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
    @PatchMapping("/update/profile")
    public ResponseEntity<String> updateProfile(@RequestBody CustomerDto customerDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");
        try {
            List<String> errors = validateCustomer(customerDto);
            if (!CollectionUtils.isEmpty(errors)) {
                return new ResponseEntity<>(String.join("\n", errors), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(customerService.updateCustomerProfile(customerDto, email), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while updating profile! ", e);
            return new ResponseEntity<>("Exception occurred while updating profile !", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //------------------------------------to update address-----------------------------------------------------------------------------
    @PutMapping("/update/address/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");

        try {
            return new ResponseEntity<>(customerService.updateAddress(addressDto, email, id), HttpStatus.OK);
        } catch (UserNotFoundException | AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException e) {
            log.error("Address not found !", e);
            return new ResponseEntity<>("Address not found!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while updating address ", e);
            return new ResponseEntity<>("Exception occurred while updating address ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-------------------------------------to update password----------------------------------------------------------------------------------
    @PatchMapping("/update/password")
    public ResponseEntity<String> updatePassword(@RequestParam @NotNull String password, @RequestParam @NotNull String confirmPassword) {

        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");
        if (!Validator.isValidatedPassword(password))
            return new ResponseEntity<>("Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number!", HttpStatus.BAD_REQUEST);

        if (!Objects.equals(password, confirmPassword))
            return new ResponseEntity<>("Your password does not match with confirm password ", HttpStatus.BAD_REQUEST);
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

    //-------------------------------------to add address------------------------------------------------------------------------------
    @PostMapping("/add/address")
    public ResponseEntity<String> addAddress(@RequestBody AddressDto addressDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null!");
        try {
            return new ResponseEntity<>(customerService.addAddress(addressDto, email), HttpStatus.OK);
        } catch (EmailNotFoundException | UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException e) {
            log.error("Address not found !", e);
            return new ResponseEntity<>("Address not found!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while adding address ", e);
            return new ResponseEntity<>("Exception occurred while adding address ", HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    //----------------------------to delete an address---------------------------------------------------------------
    @DeleteMapping("/delete/address/{id}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long id) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        try {
            return new ResponseEntity<>(customerService.deleteAddress(id), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException e) {
            log.error("Address not found !", e);
            return new ResponseEntity<>("Address not found!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while deleting address ", e);
            return new ResponseEntity<>("Exception occurred while deleting address !", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //------------------------------------customer's validation ----------------------------------------------------------------------------------
    private List<String> validateCustomer(CustomerDto customerDto) {
        List<String> errors = new ArrayList<>();

        if (customerDto.getContact() != null) {
            if (!Validator.isValidatedContact(customerDto.getContact())) {
                errors.add("Contact number must contain numeric value and must have 10 digits!!");
            }
        }
        if (customerDto.getFirstName() != null) {
            if (!(customerDto.getFirstName().length() > 2 && customerDto.getFirstName().length() <= 16)) {
                System.out.println(customerDto.getFirstName());
                System.out.println(customerDto.getFirstName().length());

                errors.add("FirstName is invalid");
            }
        }
        if (customerDto.getLastName() != null) {
            if (!(customerDto.getLastName().length() > 2 && customerDto.getLastName().length() <= 16)) {
                errors.add("LastName is invalid");
            }
        }
        return errors;
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
