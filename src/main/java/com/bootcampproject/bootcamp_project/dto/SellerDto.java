package com.bootcampproject.bootcamp_project.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class SellerDto extends UserDto {
    @NotNull(message = "GST should be valid as per govt. norms!")
    @NotBlank(message = "GST can not be blank!")
    @Pattern(regexp = "^[0-9]{2}[A-Z]{5}[0-9]{4}"
            + "[A-Z]{1}[1-9A-Z]{1}"
            + "Z[0-9A-Z]{1}$", message = "Please provide a valid GST number")
    private String gst;
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Contact number must contain numeric value and must have 10 digits!!")
    private String companyContact;

    private String companyName;
    private AddressDto address;
}
