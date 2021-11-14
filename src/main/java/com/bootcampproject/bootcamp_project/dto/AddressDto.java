package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Address;
import com.bootcampproject.bootcamp_project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String city;
    private String state;
    private String country;
    private String addressLine;
    @Size(min=6,max = 6,message = "Zip code should be of length 6!")
    private Integer zipCode;


    public AddressDto(Address address) {
        this.id = address.getId();
        this.city = address.getCity();
        this.state = address.getState();
        this.country = address.getCountry();
        this.addressLine = address.getAddressLine();
        this.zipCode = address.getZipCode();
    }

}
