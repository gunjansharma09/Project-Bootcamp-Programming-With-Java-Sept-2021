package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@RequiredArgsConstructor
@Service
@Data
public class UserService {
    @Autowired
    private UserRepository userRepository;

    //    @Autowired
//    private PasswordEncoder passwordEncoder;
    @Transactional
    public boolean isUserByEmailExists(String email) {
        return userRepository.countByEmail(email) > 0;
    }

    @Transactional
    public User save(UserDto userDto, Role role) {
//        User user= new User();
//        user.setEmail(userDto.getEmail());
        User user = User.builder()
                .email(userDto.getEmail()) // returning object of user
                .firstName(userDto.getFirstName())
                .middleName(userDto.getMiddleName())
                .lastName(userDto.getLastName())
                .password(userDto.getPassword())
                //  .password(passwordEncoder.encode(userDto.getPassword()))
                .roles(Collections.singletonList(role))
                .build();
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
