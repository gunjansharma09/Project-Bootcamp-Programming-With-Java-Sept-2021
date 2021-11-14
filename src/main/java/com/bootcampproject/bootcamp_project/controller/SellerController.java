package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.*;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.service.SellerService;
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
@RequestMapping("/seller")
@PreAuthorize("hasAnyRole('ROLE_SELLER')")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;

    //--------------------------------to view profile-----------------------------------------------------------------------------------------

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
            return new ResponseEntity<>("Exception occurred while displaying profile ", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    //-------------------------------to update profile----------------------------------------------------------------------------------------
    @PutMapping("/update/profile")
    public ResponseEntity<String> updateProfile(@RequestBody SellerDto sellerDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email cannot be null");
        try {
            List<String> errors = validateSeller(sellerDto);
            if (!CollectionUtils.isEmpty(errors)) {
                return new ResponseEntity<>(String.join("\n", errors), HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(sellerService.updateProfile(sellerDto, email), HttpStatus.OK);
        } catch (CompanyAlreadyExistsException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred in updating profile", e);
            return new ResponseEntity<>("Exception occurred in updating profile ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //----------------------------to update password------------------------------------------------------------------------------------------
    @PutMapping("/update/password")
    public ResponseEntity<?> updatePassword(@RequestParam @NotNull String password, @RequestParam @NotNull String confirmPassword) {

        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");
        if (!Validator.isValidatedPassword(password))
            return new ResponseEntity<>("Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number!", HttpStatus.BAD_REQUEST);

        if (!Objects.equals(password, confirmPassword))
            return new ResponseEntity<>("Your password does not match with confirm password ", HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(sellerService.updatePassword(email, password, confirmPassword), HttpStatus.OK);
        } catch (UserNotFoundException | NoPasswordFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (PasswordNotMatchedException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            log.error("Exception occurred while updating password!", e);
            return new ResponseEntity<>("Exception occurred while updating password! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*@PutMapping("/update/password")
    public String updatePassword(@RequestBody SetPasswordDTO setPasswordDTO) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        System.out.println(email);
        return sellerService.updatePassword(sellerDto, email);
    }*/
//---------------------------------to update address--------------------------------------------------------------------------------------
    @PutMapping("/update/address/{id}")
    public ResponseEntity<String> updateAddress(@PathVariable Long id, @RequestBody AddressDto addressDto) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (Objects.isNull(email))
            throw new EmailNotFoundException("Email can not be null");
        try {
            return new ResponseEntity<>(sellerService.updateAddress(addressDto, email, id), HttpStatus.OK);
        } catch (UserNotFoundException | AddressNotFoundException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (EntityNotFoundException e) {
            log.error("Address not found !", e);
            return new ResponseEntity<>("Address not found!", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred while updating address", e);
            return new ResponseEntity<>("Exception occurred while updating address !", HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    private List<String> validateSeller(SellerDto sellerDto) {
        List<String> errors = new ArrayList<>();
        if (sellerDto.getGst() != null) {
            if (!Validator.isValidatedGST(sellerDto.getGst())) {
                errors.add("Please provide a valid GST number");
            }
        }
        if (sellerDto.getCompanyContact() != null) {
            if (!Validator.isValidatedContact(sellerDto.getCompanyContact())) {
                errors.add("Contact number must contain numeric value and must have 10 digits!!");
            }
        }
        if (sellerDto.getFirstName() != null) {
            if (!(sellerDto.getFirstName().length() > 2 && sellerDto.getFirstName().length() <= 16)) {
                System.out.println(sellerDto.getFirstName());
                System.out.println(sellerDto.getFirstName().length());

                errors.add("FirstName is invalid");
            }
        }
        if (sellerDto.getLastName() != null) {
            if (!(sellerDto.getLastName().length() > 2 && sellerDto.getLastName().length() <= 16)) {
                errors.add("LastName is invalid");
            }
        }
        return errors;
    }
}

