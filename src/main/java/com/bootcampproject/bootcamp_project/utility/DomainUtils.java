package com.bootcampproject.bootcamp_project.utility;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Address;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DomainUtils {

    //To create a user
    public static User toUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        if (passwordEncoder != null)
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        else
            user.setPassword(userDto.getPassword());
        return user;
    }

    //To set address
    public static Address toAddress(AddressDto addressDto) {
        Address address = new Address();
        address.setZipCode(addressDto.getZipCode());
        address.setAddressLine(addressDto.getAddressLine());
        address.setCity(addressDto.getCity());
        address.setCountry(addressDto.getCountry());
        address.setState(addressDto.getState());
        return address;
    }

    //To set seller's company's details
    public static Seller toSeller(SellerDto sellerDto) {
        Seller seller = new Seller();
        seller.setCompanyContact(sellerDto.getCompanyContact());
        seller.setCompanyName(sellerDto.getCompanyName());
        seller.setGst(sellerDto.getGst());
        return seller;
    }
}
