package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {

    private String city;
    private String state;
    private String country;
    private String addressLine;
    private Integer zipCode;

}
