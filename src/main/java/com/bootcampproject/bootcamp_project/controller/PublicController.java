package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.SellerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping
public class PublicController {
    @Autowired
    private UserService userService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private SellerService sellerService;

    //--------------------------------------------------to register a customer ------------------------------------------------------------------
//    @PostMapping("/register/customer")
//    public ResponseEntity<String> registerCustomer(@Valid @RequestBody CustomerDto customerDto) {
//try
//{return new ResponseEntity<>(customerService.saveCustomer(customerDto)}
//        if (customerService.saveCustomer(customerDto))
//            return "success";
//        else return "fail";
//    }


    @PostMapping("/register/customer")
    public ResponseEntity<?> registerCustomer(@Valid @RequestBody CustomerDto customerDto, BindingResult result) {

        if (!CollectionUtils.isEmpty(result.getAllErrors())) {
            return new ResponseEntity<>(result.getAllErrors().stream().map(objectError -> {
                return objectError.getDefaultMessage();
            }).collect(Collectors.joining("\n")), HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(customerDto.getEmail()) || customerDto.getEmail().trim().equals("")) {
            return new ResponseEntity<>("No email provided ,", HttpStatus.BAD_REQUEST);
        }

        if (!Objects.equals(customerDto.getPassword(), customerDto.getConfirmPassword()))
            return new ResponseEntity<>("Your password does not match with confirm password ", HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(customerService.registerCustomer(customerDto), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            log.error("User with email " + customerDto.getEmail() + "not found");
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidGSTNumberException | InvalidPasswordException | AlreadyExistsGSTException | UserAlreadyExistsException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(" Exception occurred in register customer " + customerDto.getEmail(), e);
            return new ResponseEntity<>("Exception occurred in register seller " + customerDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //-------------------------------------------to register a seller -------------------------------------------------------------------------------
    @PostMapping("/register/seller")
    public ResponseEntity<String> registerSeller(@Valid @RequestBody SellerDto sellerDto, BindingResult result) {
        // TODO Add seller validations apart from annotations in user dto if required
        if (!CollectionUtils.isEmpty(result.getAllErrors())) {
            return new ResponseEntity<>(result.getAllErrors().stream().map(objectError -> {
                return objectError.getDefaultMessage();
            }).collect(Collectors.joining("\n")), HttpStatus.BAD_REQUEST);
        }
        if (Objects.isNull(sellerDto.getEmail()) || sellerDto.getEmail().trim().equals("")) {
            return new ResponseEntity<>("No email provided ,", HttpStatus.BAD_REQUEST);
        }

        if (!Objects.equals(sellerDto.getPassword(), sellerDto.getConfirmPassword()))
            return new ResponseEntity<>("Your password does not match with confirm password ", HttpStatus.BAD_REQUEST);
        try {
            return new ResponseEntity<>(sellerService.registerSeller(sellerDto), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            log.error("User with email " + sellerDto.getEmail() + "not found");
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (InvalidGSTNumberException | InvalidPasswordException | AlreadyExistsGSTException | UserAlreadyExistsException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error(" Exception occurred in register seller " + sellerDto.getEmail(), e);
            return new ResponseEntity<>("Exception occurred in register seller " + sellerDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //-------------------------------------to forgot password ---------------------------------------------------------------------------------
    @PostMapping("/forgot/password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {
        if (Objects.isNull(email) || email.trim().equals("")) {
            return new ResponseEntity<>("No email provided ,", HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
        } catch (UserNotFoundException userNotFoundException) {
            log.error("User with email " + email + "not found");
            return new ResponseEntity<>(userNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserDeactivateException userDeactivateException) {
            log.error("User is deactivated with email " + email);
            return new ResponseEntity<>(userDeactivateException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred in method forgotPassword() with email " + email);
            return new ResponseEntity<>("Exception occur in forgotPassword() with email " + email, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------to update password ------------------------------------------------------------------

    @PatchMapping("/set/password/{token}")
    public ResponseEntity<String> updatePassword(@PathVariable @NotBlank String token, @RequestParam @NotBlank @Size(min = 8) @Valid String password, @RequestParam @NotBlank @Size(min = 8) @Valid String confirmPassword) {
        try {
            if (!Objects.equals(password, confirmPassword))
                return new ResponseEntity<>("Your password does not match with confirm password ", HttpStatus.BAD_REQUEST);
            else
                return new ResponseEntity<>(userService.updatePassword(token, password), HttpStatus.OK);
        } catch (InvalidTokenException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (UserDeactivateException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN); // agar user mil gya h pr dwactivate h account
        } catch (InvalidPasswordException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // server ki koi galati ni h . jaha se request aai h vha se h
        } catch (Exception e) {
            log.error("Exception occurred in method updatePassword() ",e);
            return new ResponseEntity<>("Exception occurred in updatePassword() ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //---------------------------to activate customer ----------------------------------------------------------------------

    @PutMapping("/customer/activate/{token}")
    public ResponseEntity<String> activateCustomer(@PathVariable String token) {

        try {
            return new ResponseEntity<>(customerService.activateAccount(token), HttpStatus.OK);
        } catch (UserNotFoundException | InvalidTokenException e) {
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred while activating account ", e);
            return new ResponseEntity<>("Exception occurred while activating account ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
