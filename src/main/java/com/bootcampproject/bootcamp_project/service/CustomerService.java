package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.CustomerProfileDto;
import com.bootcampproject.bootcamp_project.entity.*;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.exceptions.*;
import com.bootcampproject.bootcamp_project.repository.*;
import com.bootcampproject.bootcamp_project.utility.DomainUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class CustomerService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    //--------------------------------------------To save a customer ----------------------------------------------------------
    @Transactional
    public String registerCustomer(CustomerDto customerDto) {
        Optional<User> optionalUser = userRepository.findByEmail(customerDto.getEmail());
        if (optionalUser.isPresent())
            throw new UserAlreadyExistsException("Customer registration failed. User already exists with this email id: " + customerDto.getEmail());

        User user = DomainUtils.toUser(customerDto, passwordEncoder);

        Customer customer = new Customer();
        customer.setContact(customerDto.getContact());
        customer.setCreatedBy(DomainUtils.getCreatedBy());
        customer.setUser(user);// kyonki customer me mapping ki h.. isily customer me user set kraenge.. agar mapping user me ki hoti to
        // customer me user set krane ki zarurt nahi hoti.
        user.setCustomer(customer);

        List<Address> addresses = new ArrayList<>();
        if (customerDto.getAddress() != null) {
            /*addresses = customerDto
                    .getAddress()
                    .stream()
                    .map(addressDto -> DomainUtils.toAddress(addressDto))
                    .collect(Collectors.toList());*/
            for (AddressDto addressDto : customerDto.getAddress()) {
                Address address = DomainUtils.toAddress(addressDto);
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
        user.getCustomer().setAccountActivateToken(token.toString());
        user.getCustomer().setAccountActivateTokenGeneratedAt(System.currentTimeMillis());
        user.setIsActive(customerDto.isActive());
        customerRepository.save(customer);
        sendTokenToCustomer(user.getEmail(), token);
        return "Customer registered successfully with email: " + customerDto.getEmail();

    }

    //--------------------send token to customer to activate account ------------------------------------------------------------------

    private void sendTokenToCustomer(String email, UUID uuid) {
        String url = "http://localhost:8080/customer/activate/" + uuid.toString();
        emailService.sendEmailAsync(email, "Welcome to online shopping site",
                "Hi,\nPlease activate your account by using the following link\n" + url);
    }

    // ------------------------------------to activate account--------------------------------------------------------------------------------
    @Transactional
    public String activateAccount(String token) throws UserNotFoundException {
//        if (Objects.isNull(token)) {
//            return "No token provided!";
//        }  jb b path variable se kuch b lete h to vo null kbi ni hota.. isily ye code run hi ni hoga.. isily ise ni likhenege

        Customer customer = customerRepository.findByAccountActivateToken(token);
        if (customer == null) {
            throw new InvalidTokenException("Invalid token!");
        }

        if (customer.getAccountActivateTokenGeneratedAt() < System.currentTimeMillis() - (1000 * 3600 * 3)) {
            // if (customer.getAccountActivateTokenGeneratedAt() < System.currentTimeMillis() - (1000 )) {
            UUID uuid = UUID.randomUUID();
            customer.setAccountActivateToken(uuid.toString());
            customer.setAccountActivateTokenGeneratedAt(System.currentTimeMillis());
            customerRepository.save(customer);
            sendTokenToCustomer(customer.getUser().getEmail(), uuid);
            return "Your token has expired, please check your email for new activation link";

        }

        customer.getUser().setIsActive(true);
        customer.setAccountActivateTokenGeneratedAt(null);
        customer.setAccountActivateToken(null);
        customerRepository.save(customer);
        return "Customer activated Successfully";
    }

    //---------------------------- to view profile-------------------------------------------------------------------------------------

    public CustomerProfileDto viewProfile(String email) {


        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found with eamil" + email);

        User user = optionalUser.get();

        CustomerProfileDto customerProfileDto = new CustomerProfileDto();
        customerProfileDto.setId(user.getId());
        customerProfileDto.setFirstName(user.getFirstName());
        customerProfileDto.setLastName(user.getLastName());
        customerProfileDto.setContact(user.getCustomer().getContact());
        customerProfileDto.setActive(user.getIsActive());
        customerProfileDto.setImage("image");
        return customerProfileDto;
    }

    //------------------------------------to view address-----------------------------------------------------------------

    public List<AddressDto> viewAddress(String email) {


        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User not found with eamil" + email);

        User user = optionalUser.get();

        List<AddressDto> addressDtos = new ArrayList<>();
        if (user.getAddresses() != null && user.getAddresses().size() != 0) {
            // user.getAddresses() se sare address nikalenge.. isily loop me ye lia h.. isse pehle addressDtos lia tha.. to warning thi..
            // kyonki obviously addressDtos null hoga .. just declare kia h upper.
            for (int i = 0; i < user.getAddresses().size(); i++) {
                Address address = user.getAddresses().get(i);
                if (!address.getIsDeleted()) {
                    AddressDto addressDto = new AddressDto(address);
                    addressDtos.add(addressDto);
                }
            }
        }
        return addressDtos;
    }

    //--------------------------------------to update profile-------------------------------------------------------------------------
    public String updateCustomerProfile(CustomerDto customerDto, String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("No such user found with email " + email);

        User user = optionalUser.get();

        if (customerDto.getFirstName() != null) {
            String firstName = customerDto.getFirstName();
            if (firstName.length() < 2)
                return "FirstName is invalid!";
            user.setFirstName(firstName);
        }


        if (customerDto.getLastName() != null) {
            String lastName = customerDto.getLastName();
            if (lastName.length() < 2)
                return "LastName is invalid!";
            user.setLastName(lastName);
        }

        if (customerDto.getMiddleName() != null) {
            String middleName = customerDto.getMiddleName();
            user.setMiddleName(middleName);
        }

        if (customerDto.getContact() != null) {
            user.getCustomer().setContact(customerDto.getContact());
        }

        userRepository.save(user);
        return "Profile has been updated successfully!";
    }

    //---------------------------------------to update address-------------------------------------------------------------------------

    public String updateAddress(AddressDto addressDto, String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User is not found with email " + email);
        User user = optionalUser.get();
        if (Objects.isNull(addressDto))
            throw new AddressNotFoundException("Address information is null");

        List<AddressDto> addressDtos = new ArrayList<>();

        if (user.getAddresses() != null & user.getAddresses().size() > 0) {
            for (int i = 0; i < user.getAddresses().size(); i++) {
                Address address = user.getAddresses().get(i);
                if (addressDto.getAddressLine() != null) {
                    address.setAddressLine(addressDto.getAddressLine());
                }
                if (addressDto.getState() != null) {
                    address.setState(addressDto.getState());
                }
                if (addressDto.getCity() != null) {
                    address.setCity(addressDto.getCity());
                }
                if (addressDto.getCountry() != null) {
                    address.setCountry(addressDto.getCountry());
                }
                if (addressDto.getZipCode() != null) {
                    address.setZipCode(addressDto.getZipCode());
                }
            }
            userRepository.save(user);
        }
        return "Address updated successfully!";
    }

    //-----------------------------------to update password-------------------------------------------------------------------------
    public String updatePassword(String password, String confirmPassword, String email) {


        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new UserNotFoundException("User does not exists with email " + email);
        User user = optionalUser.get();

        if (Objects.isNull(password) && Objects.isNull(confirmPassword) && !(password.length() > 2) && !(confirmPassword.length() > 0)) {
            throw new NoPasswordFoundException("Password or confirm password can not be null !");
        }
        if (!password.equals(confirmPassword))
            throw new PasswordNotMatchedException("Password does not match with confirm password!");

        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return "Password updated successfully!";
    }

    //------------------------------------to add address--------------------------------------------------------------------------------

    public String addAddress(AddressDto addressDto, String email) {


        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (!optionalUser.isPresent())
            throw new EmailNotFoundException("User does not found with email " + email);

//        Optional<User> optionalUser1 = userRepository.findById(id);
//        if (!optionalUser1.isPresent())
//            throw new UserNotFoundException("User with id " + id + " is not available!");

        User user = optionalUser.get();

        Address address = DomainUtils.toAddress(addressDto);
        user.getAddresses().add(address);
        userRepository.save(user);

        return "New Address has been added!";
    }

    //----------------------------------delete address---------------------------------------------------------------------------------
    public String deleteAddress(Long id) {
//        Optional<User> userOptional = userRepository.findById(id);
//        if (userOptional.isPresent())
//            throw new UserNotFoundException("User not found with id " + id);
        Address address = addressRepository.getById(id);
        address.setIsDeleted(true); // It is called soft delete

        //isdeleted vali valu ko set krenge true me , address ko save kra denge.. ise soft delete kehte h
        addressRepository.save(address);
        //userRepository.save(user);
        return "User's address has been deleted successfully!";

    }

    //------------------------------------------------------------------------------------------------------------------------------------
    //-------------------------------------------CATEGORY------------------------------------------------------------------------

    //Return List all root level categories if no ID is passed,else list of all immediate child nodes of passed category ID
    public List<Category> categoryList(Long id) {
        boolean exists;

        if (id != null) {
            exists = categoryRepository.existsById(id);

            if (exists) {
                List<Category> category = categoryRepository.findByParentCategory(id);
            } else {
                throw new CategoryNotFoundException("Invalid category Id");
            }
        } else {
            List<Category> category = categoryRepository.findByCategoryIsNull();
        }
        return null;
    }

    //


}

