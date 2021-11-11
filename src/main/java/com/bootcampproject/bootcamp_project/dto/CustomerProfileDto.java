package com.bootcampproject.bootcamp_project.dto;

import lombok.Data;

import java.util.List;

@Data
public class CustomerProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String contact;
    private String image;
    private List<AddressDto> addressDto;
}
