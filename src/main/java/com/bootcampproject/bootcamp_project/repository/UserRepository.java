package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;


public interface UserRepository extends CrudRepository<User, Long> {

    public Long countByEmail(String email);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByForgotPasswordToken(String forgotPasswordToken);








}


