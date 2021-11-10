package com.bootcampproject.bootcamp_project.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class SellerDto extends UserDto {
    @Column(unique = true)
    @NonNull
    private String gst ;
    private String companyContact;
    private String companyName;
    private AddressDto address;
}
