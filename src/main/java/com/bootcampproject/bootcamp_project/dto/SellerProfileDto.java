package com.bootcampproject.bootcamp_project.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SellerProfileDto {

    private Long id;
    private String firstName;
    private String lastName;
    private Boolean isActive;
    private String companyContact;
    private String companyName;
    private String image;
    @NotNull(message = "GST should be valid as per govt. norms!")
    private String gst;
    private AddressDto addressDto;
}
