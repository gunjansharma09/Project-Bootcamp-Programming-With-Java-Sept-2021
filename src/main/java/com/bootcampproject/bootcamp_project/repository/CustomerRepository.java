package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.Customer;
import com.bootcampproject.bootcamp_project.entity.Seller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    public Customer findByAccountActivateToken(String token);

    public Page<Customer> findAll(Pageable pageable);


}
