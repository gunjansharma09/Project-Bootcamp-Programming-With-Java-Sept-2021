package com.bootcampproject.bootcamp_project.utility;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Address;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.exceptions.InvalidGSTNumberException;
import com.bootcampproject.bootcamp_project.exceptions.InvalidPasswordException;
import com.bootcampproject.bootcamp_project.validator.Validator;
import org.springframework.security.crypto.password.PasswordEncoder;

public class DomainUtils {

    //To create a user
    public static User toUser(UserDto userDto, PasswordEncoder passwordEncoder) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        user.setCreatedBy(getCreatedBy());
        if (!Validator.isValidatedPassword(userDto.getPassword())) {
            throw new InvalidPasswordException("Password should contains 8-15 Characters with atleast 1 Lower case, 1 Upper case, 1 Special Character, 1 Number");
        }
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
        address.setCreatedBy(getCreatedBy());
        return address;
    }

    //To set seller's company's details
    public static Seller toSeller(SellerDto sellerDto) {
        Seller seller = new Seller();
        seller.setCompanyContact(sellerDto.getCompanyContact());
        seller.setCompanyName(sellerDto.getCompanyName());
        seller.setGst(sellerDto.getGst());
        seller.setCreatedBy(getCreatedBy());

        if (!Validator.isValidatedGST(sellerDto.getGst())) {
            throw new InvalidGSTNumberException("Please provide a valid GST number");
        }
        return seller;
    }

    public static String getCreatedBy() {
        String createdBy = SecurityContextUtil.findAuthenticatedUser();
        createdBy = (createdBy == null) ? "system" : createdBy; // bootstrap jb chalta h to vo application run hone k baad chalta h.. usme koi b user loggedin ni hota..

        return createdBy;
    }

}
