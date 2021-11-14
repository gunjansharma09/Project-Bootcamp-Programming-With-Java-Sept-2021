package com.bootcampproject.bootcamp_project.configuration;

import com.bootcampproject.bootcamp_project.entity.User;


import com.bootcampproject.bootcamp_project.exceptions.AccountLockedException;
import com.bootcampproject.bootcamp_project.exceptions.UserDeactivateException;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import com.bootcampproject.bootcamp_project.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

// customize error k lie hume ye krna hoga.
@Slf4j
public class LoginAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MyUserDetailService userDetailService;

    @Autowired
    private EmailService emailService;

    /*
        @Override
        protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                      UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {*/
    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        System.out.println("SEM>>>>>>>");
        if (authentication.getCredentials() == null) {
            logger.debug("Authentication failed: no credentials provided");

            throw new BadCredentialsException(messages.getMessage(
                    "AbstractUserDetailsAuthenticationProvider.badCredentials",
                    "Bad credentials"));
        }

        String presentedPassword = authentication.getCredentials().toString();
        System.out.println(presentedPassword);
        String principal = authentication.getPrincipal().toString();
        User user = userRepository.findByEmail(principal).orElse(null);
        if (user != null) {
            if (user.getIsLocked()) {
                throw new AccountLockedException("Your Account is locked.Please contact with support team!");
            }
            if (!user.getIsActive()) {
                throw new UserDeactivateException("Your Account is de-activated");
            }
            if (!passwordEncoder.matches(presentedPassword, user.getPassword())) {
                logger.debug("Authentication failed: password does not match stored value");

                int temp = user.getInvalidAttemptCount() != null ? user.getInvalidAttemptCount() : 0;
                user.setInvalidAttemptCount(++temp);
                if (temp == 3 && (user.getRoles().stream().noneMatch(role -> role.getAuthority().equals("ROLE_ADMIN")))) {
                    //if (temp == 3 && (!user.getRoles().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")))) {
                    user.setIsLocked(true);


                    emailService.sendEmailAsync(user.getEmail(), "Your account has been locked now", "Hi, Your account has been locked due to maximum attempt of login!");
//TODO : add send e-mail

                } else {
                    user.getRoles().forEach(role -> log.info(role.getAuthority()));
                }


                userRepository.save(user);

                throw new BadCredentialsException(
                        "Password and email doesn't match");
            }
        }
        Authentication auth = super.authenticate(authentication);
        return auth;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal,
                                                         Authentication authentication, UserDetails user) {
        System.out.println("SUCC>>>");
        User user1 = userRepository.findByEmail(user.getUsername()).orElse(null);
        if (user1 != null && user1.getInvalidAttemptCount() < 3) {
            user1.setInvalidAttemptCount(0);
            userRepository.save(user1);
        }

        return super.createSuccessAuthentication(principal, authentication, user);
    }
}
