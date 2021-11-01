package com.bootcampproject.bootcamp_project.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class SellerDto extends UserDto {
    private String gst;
    private String companyContact;
    private String companyName;
    private AddressDto address;
}
