package com.bootcampproject.bootcamp_project.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class CustomerProfileDto {
    private Long id;
    private String firstName;
    private String lastName;
    private boolean isActive;
    @NotBlank(message = "mobileNumber is required")
    @Size(min = 10, max = 10)
    private String contact;
    private String image;
    private List<AddressDto> addressDto;
}
