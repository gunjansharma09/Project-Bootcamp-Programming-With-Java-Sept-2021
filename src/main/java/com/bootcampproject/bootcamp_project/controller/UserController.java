package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/registration")
public class UserController {
//    @Autowired
//    private UserService userService;
//
//    @PostMapping("/createAccount")
//    public String save(@Valid @RequestBody UserDto userDto) {
//        if (userService.save(userDto, null) == null) {
//            return "Fail";
//        }
//        return "success";
//    }
//
//    @DeleteMapping("/deleteAccount")
//    public String delete(@RequestParam Long id) {
//        userService.delete(id);
//        return "success";
//
//    }
//
//    @PutMapping("/updateAccount")
//    public String update(@Valid @RequestBody UserDto userDto) {
//        userService.save(userDto, null);
//        return "success";
//    }


}
