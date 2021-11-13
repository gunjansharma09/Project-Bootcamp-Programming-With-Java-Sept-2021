package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Customer;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true) // to call super class values
@Data
public class CustomerResponseDTO extends UserResponseDTO {
    private String contact;

    public static CustomerResponseDTO mapper(Customer customer) {
        CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
        customerResponseDTO.setFirstName(customer.getUser().getFirstName());
        customerResponseDTO.setMiddleName(customer.getUser().getMiddleName());
        customerResponseDTO.setLastName(customer.getUser().getFirstName());
        customerResponseDTO.setEmail(customer.getUser().getEmail());
        customerResponseDTO.setActive(customer.getUser().getIsActive());
        customerResponseDTO.setLocked(customer.getUser().getIsLocked());
        customerResponseDTO.setContact(customer.getContact());
        customerResponseDTO.setId(customer.getId());
        return customerResponseDTO;
    }

}
