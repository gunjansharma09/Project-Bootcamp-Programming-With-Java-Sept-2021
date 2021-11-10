package com.bootcampproject.bootcamp_project.dto;

import lombok.Data;

@Data
public class SellerProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String companyContact;
    private String companyName;
    private String image;
    private String gst;
    private AddressDto addressDto;
}
