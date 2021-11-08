package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.entity.Customer;
import com.bootcampproject.bootcamp_project.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer save(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());
        return customerRepository.save(customer);
    }
    @Transactional
    public String delete(Long id) {
        customerRepository.deleteById(id);
        return "success";
    }

}
