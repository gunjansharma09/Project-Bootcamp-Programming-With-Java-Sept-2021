package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.repository.CustomerRepository;
import com.bootcampproject.bootcamp_project.repository.RoleRepository;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import com.bootcampproject.bootcamp_project.utility.DomainUtils;
import org.springframework.beans.factory.annotation.Autowired;
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


    private void sendTokenViaEmailToUser(String email, UUID uuid) {
        String url = "http://localhost:8080/set/password/" + uuid.toString();
        emailService.sendEmailAsync(email, "Welcome to online shopping site", "Hi,\nUse link mentioned below to restore your password\n" + url);
    }

    public String forgotPassword(String email) {
        if (Objects.isNull(email))
            return "No email provided";

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent())
            return "Invalid email address";

        User user = userOptional.get();

        if (user.getForgotPasswordToken() == null) {
            sendToken(user);
            return "Password restore link has been sent over email!";
        } else {
            sendToken(user);
            return "A new email has been sent with a link set your new password.";
        }
    }

    private void sendToken(User user) {
        UUID uuid = UUID.randomUUID();
        user.setForgotPasswordToken(uuid.toString());
        user.setForgotPasswordGeneratedTokenAt(System.currentTimeMillis());
        userRepository.save(user);
        sendTokenViaEmailToUser(user.getEmail(), uuid);
    }


    public String updatePassword(String token, String newPassword) {
        //Case 1: Invalid token
        Optional<User> userOptional = userRepository.findByForgotPasswordToken(token);
        if (!userOptional.isPresent())
            return "Invalid token!";

        //Case 1 completed

        User user = userOptional.get();

        //Case 2: Expired token

        if (user.getForgotPasswordGeneratedTokenAt() < System.currentTimeMillis() - (1 * 60 * 1000)) {
            sendToken(user);
            return "Your token has expired. A new token has been shared over email.";
        }
        //Case 2 completed

        //Case 3: Happy Flow
        user.setPassword(newPassword);
        user.setForgotPasswordToken(null);
        user.setForgotPasswordGeneratedTokenAt(null);
        userRepository.save(user);
        return "Your password is successfully created!";
        //Case 3 completed
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}





