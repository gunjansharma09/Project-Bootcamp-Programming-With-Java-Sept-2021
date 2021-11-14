package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.exceptions.InvalidPasswordException;
import com.bootcampproject.bootcamp_project.exceptions.RegistrationFailedException;
import com.bootcampproject.bootcamp_project.exceptions.UserNotFoundException;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.SellerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

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
//    public String registerCustomer(@Valid @RequestBody CustomerDto customerDto) {
//        if (customerService.saveCustomer(customerDto))
//            return "success";
//        else return "fail";
//    }

    //-------------------------------------------to register a seller -------------------------------------------------------------------------------
    @PostMapping("/register/seller")
    public ResponseEntity<String> registerSeller(@Valid @RequestBody SellerDto sellerDto) {
        // TODO Add seller validations apart from annotations in user dto if required
        return sellerService.saveSeller(sellerDto);
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
        } catch (Exception e) {
            log.error("Exception occurred in method forgotPassword() with email " + email);
            return new ResponseEntity<>("Exception occur in forgotPassword() with email " + email, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------to update password ------------------------------------------------------------------

    @PutMapping("set/password")
    public ResponseEntity<String> updatePassword(@RequestParam @NotBlank String token, @RequestParam @NotBlank @Size(min = 8) @Valid String password) {
        try {
            return new ResponseEntity<>(userService.updatePassword(token, password), HttpStatus.OK);
        } catch (InvalidPasswordException invalidPasswordException) {
            log.error("Passowrd must contain 1 Upper case, 1 lower case , 1 special symbol and 1 number ");
            return new ResponseEntity<>(invalidPasswordException.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Exception occurred in method updatePassword() ");
            return new ResponseEntity<>("Exception occurred in updatePassowrd() ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //---------------------------to activate customer ----------------------------------------------------------------------

    @GetMapping("/customer/activate/{token}")
    public ResponseEntity<String> activateCustomer(@PathVariable String token) {
        if (Objects.isNull(token) || token.trim().equals("")) {
            return new ResponseEntity<>("No email provided ,", HttpStatus.BAD_REQUEST);
        }
        try {
            return new ResponseEntity<>(customerService.activateAccount(token), HttpStatus.OK);
        } catch (UserNotFoundException e) {
            log.error("Exception occurred while activating account ");
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Exception occurred while activating account ");
            return new ResponseEntity<>("Exception occurred while activating account ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
