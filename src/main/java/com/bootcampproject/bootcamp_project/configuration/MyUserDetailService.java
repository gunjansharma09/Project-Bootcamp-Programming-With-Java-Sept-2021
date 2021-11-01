package com.bootcampproject.bootcamp_project.configuration;

import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(userName);
        //if (user == null)
        //  throw new UsernameNotFoundException("Invalid name or password!");

        //return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), null);
        return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException("Invalid name or password!"));

//        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
//
//        return user.map(MyUserDetails::new).get();

    }
}
