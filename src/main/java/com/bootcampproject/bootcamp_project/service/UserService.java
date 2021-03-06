package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.exceptions.InvalidPasswordException;
import com.bootcampproject.bootcamp_project.exceptions.InvalidTokenException;
import com.bootcampproject.bootcamp_project.exceptions.UserDeactivateException;
import com.bootcampproject.bootcamp_project.exceptions.UserNotFoundException;
import com.bootcampproject.bootcamp_project.repository.CustomerRepository;
import com.bootcampproject.bootcamp_project.repository.RoleRepository;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import com.bootcampproject.bootcamp_project.utility.DomainUtils;
import com.bootcampproject.bootcamp_project.validator.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private CustomerRepository customerRepository;


    @Transactional
    public boolean isUserByEmailExists(String email) {
        return userRepository.countByEmail(email) > 0;
    }

    //--------------------------------to save admin-------------------------------------------------------------------------------
    public Boolean saveAdmin(UserDto userDto) {
        User user = DomainUtils.toUser(userDto, passwordEncoder);
        user.setIsActive(userDto.isActive());
        Role role = roleRepository.findByAuthority(RoleEnum.ROLE_ADMIN.name());
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    //------------------ to send token via email to user ----------------------------------------------------------------------------------------
    private void sendTokenViaEmailToUser(String email, UUID uuid, String subject) {
        String url = "http://localhost:8080/set/password/" + uuid.toString();
        emailService.sendEmailAsync(email, subject, "Hi,\nUse link mentioned below to restore your password\n" + url);
    }

    //-----------------------------------------------to forgot password via email----------------------------------------------------------------------
    public String forgotPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent())
            throw new UserNotFoundException("User not found with email " + email);
        User user = userOptional.get();

        if (!user.getIsActive())
            throw new UserDeactivateException("User account is deactivate ");


        if (user.getForgotPasswordToken() == null) {
            sendToken(user, "Reset password");
            return "Password restore link has been sent over email!";
        } else {
            sendToken(user, "New password link"); // change, use subject here
            return "A new email has been sent with a link set your new password.";
        }
    }

    //-----------------------------------------to send token----------------------------------------------------------------------------------

    private void sendToken(User user, String subject) {
        UUID uuid = UUID.randomUUID();
        user.setForgotPasswordToken(uuid.toString());
        user.setForgotPasswordGeneratedTokenAt(System.currentTimeMillis());
        userRepository.save(user);
        sendTokenViaEmailToUser(user.getEmail(), uuid, subject);
    }

    // -------------------------------to update password----------------------------------------------------------------------------------------
    public String updatePassword(String token, String newPassword) {
        //Case 1: Invalid token
        Optional<User> userOptional = userRepository.findByForgotPasswordToken(token);
        if (!userOptional.isPresent())
            throw new InvalidTokenException("Invalid token!");

        //Case 1 completed

        User user = userOptional.get();

        if (!user.getIsActive())
            throw new UserDeactivateException("User is deactivated! ");

        if (!Validator.isValidatedPassword(newPassword))
            throw new InvalidPasswordException("Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number!");

        //Case 2: Expired token

        if (user.getForgotPasswordGeneratedTokenAt() < System.currentTimeMillis() - (15 * 60 * 1000)) {
            sendToken(user, "");
            return "Your token has expired. A new token has been shared over email.";
        }
        //Case 2 completed

        //Case 3: Happy Flow
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setForgotPasswordToken(null);
        user.setForgotPasswordGeneratedTokenAt(null);
        userRepository.save(user);

        emailService.sendEmailAsync(user.getEmail(), "Password successfully updated!", "");

        return "Your password is successfully updated!";
        //Case 3 completed
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}





