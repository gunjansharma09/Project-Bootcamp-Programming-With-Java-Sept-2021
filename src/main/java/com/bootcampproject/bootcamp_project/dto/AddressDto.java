package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Address;
import com.bootcampproject.bootcamp_project.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@NoArgsConstructor
public class AddressDto {

    private String city;
    private String state;
    private String country;
    private String addressLine;
    private Integer zipCode;

    public AddressDto(Address address) {
        this.city = address.getCity();
        this.state = address.getState();
        this.country = address.getCountry();
        this.addressLine = address.getAddressLine();
        this.zipCode = address.getZipCode();
    }

}
