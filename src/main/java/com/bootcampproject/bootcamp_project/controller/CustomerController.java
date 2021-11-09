package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String save(@Valid @RequestBody CustomerDto customerDto) {
        if (userService.saveCustomer(customerDto))
            return "success";
        else return "fail";
    }

//    @DeleteMapping("/delete")
//    public void delete(@RequestParam Long id) {
//        customerService.delete(id);
//    }
//
//    @PutMapping("/update")
//    public String update(@Valid @RequestBody CustomerDto customerDto) {
//        customerService.save(customerDto);
//        return "success";
//    }

    @GetMapping("/read")
    public String read() {
        return "Hello customer";
    }

    @GetMapping("/activate/{token}")
    public String activateCustomer(@PathVariable String token) {
        return userService.activateAccount(token);
    }

}
