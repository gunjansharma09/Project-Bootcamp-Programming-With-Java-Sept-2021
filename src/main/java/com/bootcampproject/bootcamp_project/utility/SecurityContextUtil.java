package com.bootcampproject.bootcamp_project.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityContextUtil {
    // Security context holds details of current user.
    public static String findAuthenticatedUser() {
        //Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Object principal = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        } else {
            principal = authentication.getPrincipal();
        }

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        } else {
            return null;
        }
    }
}
