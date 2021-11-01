package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping("/save")
    public String save(@Valid @RequestBody CustomerDto customerDto) {
        if (customerService.save(customerDto) == null)
            return "fail";
        else return "success";
    }

    @DeleteMapping("/delete")
    public void delete(@RequestParam Long id) {
        customerService.delete(id);
    }

    @PutMapping("/update")
    public String update(@Valid @RequestBody CustomerDto customerDto) {
        customerService.save(customerDto);
        return "success";
    }

    @GetMapping("/read")
    public String read() {
        return "Hello customer";
    }

}
