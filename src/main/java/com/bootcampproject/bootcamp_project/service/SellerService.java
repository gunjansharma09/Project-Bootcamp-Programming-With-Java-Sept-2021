package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.dto.SellerProfileDto;
import com.bootcampproject.bootcamp_project.entity.Address;
import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.exceptions.PasswordNotMatchedException;
import com.bootcampproject.bootcamp_project.exceptions.UserNotFoundException;
import com.bootcampproject.bootcamp_project.repository.RoleRepository;
import com.bootcampproject.bootcamp_project.repository.SellerRepository;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import com.bootcampproject.bootcamp_project.utility.DomainUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service

@RequiredArgsConstructor
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private EmailService emailService;

    //-------------------------------to save seller details--------------------------------------------------------------------------------
    @Transactional
    public Boolean saveSeller(SellerDto sellerDto) {
        User user = DomainUtils.toUser(sellerDto, passwordEncoder);
        Seller seller = DomainUtils.toSeller(sellerDto);
        seller.setUser(user);
        user.setSeller(seller);

        List<Address> addresses = new ArrayList<Address>();
        if (sellerDto.getAddress() != null) {
            Address address = DomainUtils.toAddress(sellerDto.getAddress());
            addresses.add(address);
        }
        user.setAddresses(addresses);

        List<Role> roles = roleService.createRoles(RoleEnum.ROLE_SELLER);
        user.setRoles(roles);
        user.setIsActive(sellerDto.isActive());
        userRepository.save(user);
        emailService.sendEmailAsync(user.getEmail(), "Welcome to online shopping site", "Hi,\\nWaiting for approval");
        return true;
    }

    // --------------------------------------to view profile-----------------------------------------------------------------------

    public SellerProfileDto viewProfile(String email) {
        if (Objects.isNull(email))
            throw new NullPointerException("No email provided");


        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found with email: " + email);

        User user = optionalUser.get();

        SellerProfileDto sellerProfileDto = new SellerProfileDto();
        sellerProfileDto.setCompanyName(user.getSeller().getCompanyName());
        sellerProfileDto.setCompanyContact(user.getSeller().getCompanyContact());
        sellerProfileDto.setFirstName(user.getFirstName());
        sellerProfileDto.setLastName(user.getLastName());
        sellerProfileDto.setGst(user.getSeller().getGst());
        sellerProfileDto.setIsActive(user.getIsActive());
        sellerProfileDto.setId(user.getId());
        if (user.getAddresses() != null && user.getAddresses().size() != 0) {
            Address address = user.getAddresses().get(0);
            AddressDto addressDto = new AddressDto(address);
            sellerProfileDto.setAddressDto(addressDto);
        }
        sellerProfileDto.setImage("image");
        return sellerProfileDto;
    }

    //--------------------to update profile-------------------------------------------------------------------------------

    public String update(SellerDto sellerDto, String email) {
        // SellerProfileDto sellerProfileDto = new SellerProfileDto();
        if (Objects.isNull(email))
            throw new NullPointerException("Email cannot be null");

        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found with email: " + email);

        User user = optionalUser.get();

        if (sellerDto.getCompanyName() != null) {

            user.getSeller().setCompanyName(sellerDto.getCompanyName());
        }

        if (sellerDto.getCompanyContact() != null) {

            user.getSeller().setCompanyContact(sellerDto.getCompanyContact());
        }

        if (sellerDto.getFirstName() != null) {
            String firstName = sellerDto.getFirstName();
            if (firstName.length() < 2)
                return "FirstName is invalid";
            user.setFirstName(firstName);
        }

        if (sellerDto.getLastName() != null) {
            String lastName = sellerDto.getLastName();
            if (lastName.length() < 2)
                return "Last name is invalid";
            user.setLastName(lastName);
        }

        if (sellerDto.getGst() != null) {
            user.getSeller().setGst(sellerDto.getGst());
        }
        userRepository.save(user);

        return "successfully updated!";

    }


    //------------------------------to update password----------------------------------------------------------------------------------
    public String updatePassword(String email, String password, String confirmPassword) {
        if (Objects.isNull(email))
            throw new NullPointerException("Email can not be null");

        Optional<User> optionalUser = userRepository.findByEmail(email);
        // Optional user ho b sakta h nahi b.. isily optional <User> use krte h.
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found with email " + email);

        if (Objects.isNull(password) && Objects.isNull(confirmPassword) && password.length() > 0 && confirmPassword.length() > 0)
            throw new NullPointerException("Password or confirm password field cannot be null!");

        if (!password.equals(confirmPassword))
            throw new PasswordNotMatchedException("Password and Confirm Password are not matching");

        User user = optionalUser.get();
        user.setPassword(passwordEncoder.encode(password));
        return "password has been updated successfully!";
    }

    //---------------------to update address-------------------------------------------------------------------------------------------

    public String updateAddress(AddressDto addressDto, String email) {
        if (Objects.isNull(email))
            throw new NullPointerException("Email can not be null");
        Optional<User> optionalUser = userRepository.findByEmail(email);
        //  user ho b sakta h nahi b.. isily optional <User> use krte h.
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User is not found with email " + email);
        User user = optionalUser.get();

        if (Objects.isNull(addressDto))
            throw new NullPointerException("Address information is null");

        if (user.getAddresses() != null && user.getAddresses().size() > 0) {
            Address address = user.getAddresses().get(0);

            if (addressDto.getAddressLine() != null) {
                address.setAddressLine(addressDto.getAddressLine());
            }
            if (addressDto.getCountry() != null) {
                address.setCountry(addressDto.getCountry());
            }
            if (addressDto.getCity() != null) {
                address.setCity(addressDto.getCity());
            }
            if (addressDto.getState() != null) {
                address.setState(addressDto.getState());
            }
            if (addressDto.getZipCode() != null) {
                address.setZipCode(addressDto.getZipCode());
            }

            userRepository.save(user);
        }
        return "Successfully updated address!";
    }

}
