package com.bootcampproject.bootcamp_project.bootstrap;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.service.CustomerService;
import com.bootcampproject.bootcamp_project.service.RoleService;
import com.bootcampproject.bootcamp_project.service.SellerService;
import com.bootcampproject.bootcamp_project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@Slf4j
@AllArgsConstructor
public class Bootstrap implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;
    private final SellerService sellerService;
    private final CustomerService customerService;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>>>>>Save boostrap data>>>>");
        String adminEmail = "admin@tothenew.com";
        String customerEmail = "shweta.goel@tothenew.com";
        String sellerEmail = "projectfirst96@gmail.com";
        Role roleAdmin = null;
        Role roleSeller = null;
        Role roleCustomer = null;
        if (!roleService.isRoleExists(RoleEnum.ROLE_ADMIN.name())) {
            roleAdmin = roleService.saveRole(RoleEnum.ROLE_ADMIN.name());
        }
        if (!roleService.isRoleExists(RoleEnum.ROLE_SELLER.name())) {
            roleSeller = roleService.saveRole(RoleEnum.ROLE_SELLER.name());
        }
        if (!roleService.isRoleExists(RoleEnum.ROLE_CUSTOMER.name())) {
            roleCustomer = roleService.saveRole(RoleEnum.ROLE_CUSTOMER.name());
        }
        if (!userService.isUserByEmailExists(adminEmail)) {
            UserDto userDto = new UserDto();
            userDto.setEmail(adminEmail);
            userDto.setFirstName("Admin");
            userDto.setLastName("Admin");
            userDto.setPassword("Admin@12");
            userDto.setActive(true);
            userService.saveAdmin(userDto);
        }

        if (!userService.isUserByEmailExists(customerEmail)) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setEmail(customerEmail);
            customerDto.setFirstName("Customer");
            customerDto.setLastName("Customer");
            customerDto.setPassword("Customer@12");
            customerDto.setActive(false);
            customerDto.setAddress(new ArrayList<AddressDto>());
            customerService.saveCustomer(customerDto);
        }

        if (!userService.isUserByEmailExists(sellerEmail)) {
            SellerDto sellerDto = new SellerDto();
            sellerDto.setEmail(sellerEmail);
            sellerDto.setFirstName("Seller");
            sellerDto.setLastName("Seller");
            sellerDto.setPassword("Seller@12");
            sellerDto.setActive(false);
            sellerDto.setAddress(new AddressDto());
            sellerService.saveSeller(sellerDto);
        }



        log.info(">>>>>>Done saving boostrap data>>>>");


    }
}





