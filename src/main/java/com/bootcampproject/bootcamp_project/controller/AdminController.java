package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.service.AdminService;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @Autowired
    private CustomerService customerService;

    // API to find all the sellers
    @GetMapping("/list/sellers")
    public ResponseEntity<?> viewAllSeller(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageOffset, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String email) {
        try {
            return ResponseEntity.ok(adminService.findListOfSellers(pageSize, pageOffset, sortBy, email));
        } catch (Exception e) {
            log.error("Exception occurred while fetching seller list ,", e);
            return new ResponseEntity<>("Exception occurred while fetching seller list ,", HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------
    // API to find all the customers
    @GetMapping("/list/customers")
    public ResponseEntity<?> viewAllCustomer(@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Integer pageOffSet, @RequestParam(required = false) String sortBy, @RequestParam(required = false) String email) {
        try {
            return ResponseEntity.ok(adminService.findListOfCustomer(pageSize, pageOffSet, sortBy, email));
        } catch (Exception e) {
            log.error("Exception occurred while fetching customer list,", e);
            return new ResponseEntity<>("Exception occurred while fetching customer list,", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------
    // ------------------------------------api to activate/deactivate customer--------------------------------------------------------------------
    @PatchMapping("/customer/activate/{id}")
    public ResponseEntity<?> activeCustomerByAdmin(@PathVariable Long id, @RequestParam boolean isActive) {
        try {
            return adminService.activateDeactivateCustomerAccountByAdmin(id, isActive);
        } catch (Exception e) {
            log.error("Exception occurred while activating customer ,", e);
            return new ResponseEntity<>("Exception occurred while activating customer , ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //    @PutMapping("/customer/deactivate/{id}")
//    public ResponseEntity<?> deactiveCustomerByAdmin(@PathVariable Long id) {
//        try {
//            return adminService.deactivateCustomerAccountByAdmin(id);
//        } catch (Exception e) {
//            log.error("Exception occurred wgile deactivating customer , ", e);
//            return new ResponseEntity<>("Exception occurred wgile deactivating customer ,", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//    }
// -----------------------------api to activate/deactivate seller -----------------------------------------------------------------------------------------
    @PatchMapping("/seller/activate/{id}")
    public ResponseEntity<?> activeSellerByAdmin(@PathVariable Long id, @RequestParam Boolean isActive) {
        try {
            return adminService.activateDeactivateSellerAccountByAdmin(id, isActive);
        } catch (Exception e) {
            log.error("Exception occurred while activating seller ,", e);
            return new ResponseEntity<>("Exception occurred while activating seller ! ", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //-----------------------------------api yo lock / unlock customer--------------------------------------------------------------------------------
    @PatchMapping("/customer/lockunlock/{id}")
    public ResponseEntity<?> lockUnlockCustomer(@PathVariable Long id, @RequestParam boolean isLocked) {
        try {
            return adminService.isCustomerLocked(id, isLocked);
        } catch (Exception e) {
            log.error("Exception occurred while unlocking customer ,", e);
            return new ResponseEntity<>("Exception occurred while unlocking customer , ", HttpStatus.LOCKED);
        }
    }

    @PatchMapping("/seller/lockunlock/{id}")
    public ResponseEntity<?> lockUnlockSeller(@PathVariable Long id, @RequestParam boolean isLocked) {
        try {
            return adminService.isSellerLocked(id, isLocked);
        } catch (Exception e) {
            log.error("Exception occurred while unlocking seller ,", e);
            return new ResponseEntity<>("Exception occurred while unlocking seller ", HttpStatus.LOCKED);
        }
    }
    //
//    @PutMapping("/seller/deactivate/{id}")
//    public String deactiveSellerByAdmin(@PathVariable Long id) {
//        return adminService.deactivateSellerAccountByAdmin(id);
//    }

}
