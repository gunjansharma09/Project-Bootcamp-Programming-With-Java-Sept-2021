package com.bootcampproject.bootcamp_project.controller;


import com.bootcampproject.bootcamp_project.utility.SecurityContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class LogoutController {

    @Autowired
    TokenStore tokenStore;

    @PostMapping("/dologout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        String email = SecurityContextUtil.findAuthenticatedUser();
        if (email == null) {
            return new ResponseEntity<>("Email not found", HttpStatus.NOT_FOUND);
        }
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replaceAll("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
    }
}
