package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.*;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.repository.CustomerRepository;
import com.bootcampproject.bootcamp_project.repository.RoleRepository;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class UserService implements UserServiceInterface {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    private ExecutorService executorService;

    @Autowired
    private CustomerRepository customerRepository;

    public UserService() {
        this.executorService = Executors.newSingleThreadExecutor();
    }

    @Transactional
    public boolean isUserByEmailExists(String email) {
        return userRepository.countByEmail(email) > 0;
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private User create(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setMiddleName(userDto.getMiddleName());
        user.setLastName(userDto.getLastName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return user;
    }

    @Transactional
    @Override
    public Boolean saveCustomer(CustomerDto customerDto) {
        User user = create(customerDto);

        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());
        customer.setUser(user);// kyonki customer me mapping ki h.. isily customer me user set kraenge.. agar mapping user me ki hoti to
        // customer me user set krane ki zarurt nahi hoti.
        user.setCustomer(customer);

        List<Address> addresses = new ArrayList<Address>();
        if (customerDto.getAddress() != null) {
            for (AddressDto addressDto : customerDto.getAddress()) {
                Address address = new Address();
                address.setZipCode(addressDto.getZipCode());
                address.setAddressLine(addressDto.getAddressLine());
                address.setCity(addressDto.getCity());
                address.setCountry(addressDto.getCountry());
                address.setState(addressDto.getState());
                addresses.add(address);
            }
        }

        user.setAddresses(addresses);

        Role role = roleRepository.findByAuthority(RoleEnum.ROLE_CUSTOMER.name());
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        //generating random token and set that token and current time in user.sending user's email id and token.
        UUID token = UUID.randomUUID();
        user.getCustomer().setToken(token.toString());
        user.getCustomer().setTokenGenerated(System.currentTimeMillis());
        userRepository.save(user);
        sendTokenViaEmail(user.getEmail(), token);
        return true;
    }

    private void sendTokenViaEmail(String email, UUID uuid) {
        String url = "http://localhost:8080/customer/activate/" + uuid.toString();
        executorService.submit(() -> {
            emailService.sendSimpleMessage(email, "Welcome to online shopping site", "Hi,\nPlease activate your account by using the following link\n" + url);
        });
    }

    @Transactional
    @Override
    public Boolean saveSeller(SellerDto sellerDto) {
        User user = create(sellerDto);

        Seller seller = new Seller();
        seller.setCompanyContact(sellerDto.getCompanyContact());
        seller.setCompanyName(sellerDto.getCompanyName());
        seller.setGst(sellerDto.getGst());
        seller.setUser(user);
        user.setSeller(seller);
        List<Address> addresses = new ArrayList<Address>();

        if (sellerDto.getAddress() != null) {
            Address address = new Address();
            address.setState(sellerDto.getAddress().getState());
            address.setAddressLine(sellerDto.getAddress().getAddressLine());
            address.setCountry(sellerDto.getAddress().getCountry());
            address.setCity(sellerDto.getAddress().getCity());
            address.setZipCode(sellerDto.getAddress().getZipCode());
            addresses.add(address);
        }
        user.setAddresses(addresses);

        Role role = roleRepository.findByAuthority(RoleEnum.ROLE_SELLER.name());
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        sendEmail(user.getEmail());
        return true;
    }


    private void sendEmail(String email) {
        emailService.sendSimpleMessage(email, "Welcome to online shopping site", "Hi,\\nWaiting for approval");
    }

    public Boolean saveAdmin(UserDto userDto) {
        User user = create(userDto);
        user.setIsActive(userDto.isActive());
        Role role = roleRepository.findByAuthority(RoleEnum.ROLE_ADMIN.name());
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }


    @Transactional
    public String activateAccount(String token) {
        if (Objects.isNull(token)) {
            return "No token provided!";
        }

        Customer customer = customerRepository.findByToken(token);
        if (customer == null) {
            return "No customer found with this token!";
        }

        if (customer.getTokenGenerated() < System.currentTimeMillis() - (1 * 60 * 1000)) {
            UUID uuid = UUID.randomUUID();
            customer.setToken(uuid.toString());
            customer.setTokenGenerated(System.currentTimeMillis());
            customerRepository.save(customer);
            sendTokenViaEmail(customer.getUser().getEmail(), uuid);
            return "Your token has expired, please check your email for new activation link";
        }

        customer.getUser().setIsActive(true);
        customer.setTokenGenerated(null);
        customer.setToken(null);
        userRepository.save(customer.getUser());
        return "Customer activated Successfully";
    }


    private void sendTokenViaEmailToUser(String email, UUID uuid) {
        String url = "http://localhost:8080/registration/update-password/" + uuid.toString();
        executorService.submit(() -> {
            emailService.sendSimpleMessage(email, "Welcome to online shopping site", "Hi,\nUse link mentioned below to restore your password\n" + url);
        });
    }

    public String passwordRestore(String email) {
        if (Objects.isNull(email))
            return "No email provided";

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent())
            return "Invalid email address";

        User user = userOptional.get();

        if (user.getForgotPasswordToken() == null) {
            sendToken(user);
            return "Password restore link has been sent over email!";
        } else {
            sendToken(user);
            return "A new email has been sent with a link set your new password.";
        }
    }

    private void sendToken(User user) {
        UUID uuid = UUID.randomUUID();
        user.setForgotPasswordToken(uuid.toString());
        user.setForgotPasswordGeneratedTokenAt(System.currentTimeMillis());
        userRepository.save(user);
        sendTokenViaEmailToUser(user.getEmail(), uuid);
    }


    public String updatePassword(String token, String newPassword) {
        //Case 1: Invalid token
        Optional<User> userOptional = userRepository.findByForgotPasswordToken(token);
        if (!userOptional.isPresent())
            return "Invalid token!";

        //Case 1 completed

        User user = userOptional.get();

        //Case 2: Expired token

        if (user.getForgotPasswordGeneratedTokenAt() < System.currentTimeMillis() - (1 * 60 * 1000)) {
            sendToken(user);
            return "Your token has expired. A new token has been shared over email.";
        }
        //Case 2 completed

        //Case 3: Happy Flow
        user.setPassword(newPassword);
        user.setForgotPasswordToken(null);
        user.setForgotPasswordGeneratedTokenAt(null);
        userRepository.save(user);
        return "Your password is successfully created!";
        //Case 3 completed
    }
}





