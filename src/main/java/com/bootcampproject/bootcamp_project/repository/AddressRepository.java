package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Long> {
}
