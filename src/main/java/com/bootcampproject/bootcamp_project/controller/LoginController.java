package com.bootcampproject.bootcamp_project.controller;

import com.bootcampproject.bootcamp_project.dto.LoginDto;
import io.swagger.models.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
public class LoginController {
    @GetMapping("/user/registration")
    public String showRegistrationForm(@Valid WebRequest request, Model model) {
        LoginDto userDto = new LoginDto();
    //    model.addAttribute("user", userDto);
        return "registration";
    }
}
