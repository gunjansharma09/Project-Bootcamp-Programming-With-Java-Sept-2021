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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

//    @Transactional
//    public User save(UserDto userDto, Role role) {
////        User user= new User();
////        user.setEmail(userDto.getEmail());
//        User user = User.builder()
//                .email(userDto.getEmail()) // returning object of user
//                .firstName(userDto.getFirstName())
//                .middleName(userDto.getMiddleName())
//                .lastName(userDto.getLastName())
//                //.password(userDto.getPassword())
//                .password(passwordEncoder.encode(userDto.getPassword()))
//                .roles(Collections.singletonList(role))
//                .build();
//        user.setIsActive(true);
//
//        return userRepository.save(user);
//    }


    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Transactional
    @Override
    public Boolean saveCustomer(CustomerDto customerDto) {

        User user = new User();
        user.setEmail(customerDto.getEmail());
        user.setFirstName(customerDto.getFirstName());
        user.setMiddleName(customerDto.getMiddleName());
        user.setLastName(customerDto.getLastName());
        user.setPassword(customerDto.getPassword());

        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());
        customer.setUser(user);// kyonki cuctomer me mapping ki h.. isily customerme user set kraenge.. agar mapping user me ki hoti to
        // customer me user set krane ki zarurt nahi hoti.
        user.setCustomer(customer);

        List<Address> addresses = new ArrayList<Address>();
        for (AddressDto addressDto : customerDto.getAddress()) {
            Address address = new Address();
            address.setZipCode(addressDto.getZipCode());
            address.setAddressLine(addressDto.getAddressLine());
            address.setCity(addressDto.getCity());
            address.setCountry(addressDto.getCountry());
            address.setState(addressDto.getState());
            addresses.add(address);
        }
        user.setAddresses(addresses);

        Role role = roleRepository.findByAuthority(RoleEnum.CUSTOMER.name());
        List<Role> roles = new ArrayList<>();
        roles.add(role);
        user.setRoles(roles);

        UUID token = UUID.randomUUID();
        user.getCustomer().setToken(token.toString());
        user.getCustomer().setTokenGenerated(System.currentTimeMillis());
        userRepository.save(user);
        sendTokenViaEmail(user.getEmail(), token);
        return true;
    }

    @Transactional
    @Override
    public Boolean saveSeller(SellerDto sellerDto) {
        User user = new User();
        user.setEmail(sellerDto.getEmail());
        user.setFirstName(sellerDto.getFirstName());
        user.setMiddleName(sellerDto.getMiddleName());
        user.setLastName(sellerDto.getLastName());
        user.setPassword(sellerDto.getPassword());

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

        Role role = roleRepository.findByAuthority(RoleEnum.SELLER.name());
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    public Boolean saveAdmin(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setPassword(userDto.getPassword());


        Role role = roleRepository.findByAuthority(RoleEnum.ADMIN.name());
        List<Role> roles = new ArrayList<Role>();
        roles.add(role);
        user.setRoles(roles);
        userRepository.save(user);
        return true;
    }

    private void sendTokenViaEmail(String email, UUID uuid) {
        String url = "http://localhost:8080/customer/activate/" + uuid.toString();
        executorService.submit(() -> {
            emailService.sendSimpleMessage(email, "Welcome to online shopping site", "Hi,\nPlease activate your account by using the following link\n" + url);
        });
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
}

