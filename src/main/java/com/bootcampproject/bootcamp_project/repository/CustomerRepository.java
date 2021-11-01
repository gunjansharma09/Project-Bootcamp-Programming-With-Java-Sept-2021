package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
}
