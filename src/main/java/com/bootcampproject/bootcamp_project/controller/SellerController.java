package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.service.SellerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/seller")
public class SellerController {
    @Autowired
    private SellerService sellerService;

    @Autowired
    private UserService userService;


    @PostMapping("/create")
    public String save(@Valid @RequestBody SellerDto sellerDto) {
        if (sellerService.save(sellerDto) == null)
            return "fail";
        else return "success";
    }

    @DeleteMapping("/delete")
    public void deleteSeller(@RequestParam Long id) {
        sellerService.deleteById(id);
    }

    @PutMapping("/update")
    public String update(@Valid @RequestBody SellerDto sellerDto) {
        sellerService.save(sellerDto);
        return "success";
    }

    @GetMapping("/read")
    public String read() {
        return "Hello seller";
    }


}
