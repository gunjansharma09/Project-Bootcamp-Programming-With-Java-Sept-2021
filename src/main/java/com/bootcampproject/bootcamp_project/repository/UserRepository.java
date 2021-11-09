package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.Customer;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    public Long countByEmail(String email);

    public Optional<User> findByEmail(String email);

    public Optional<User> findByForgotPasswordToken(String forgotPasswordToken);

//    public List<Seller> findAllSellers();
//
//    public List<Customer> findAllCustomer();




}


