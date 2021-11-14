package com.bootcampproject.bootcamp_project.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class SellerDto extends UserDto {
    @NotNull
    private String gst ;
    private String companyContact;
    private String companyName;
    private AddressDto address;
}
