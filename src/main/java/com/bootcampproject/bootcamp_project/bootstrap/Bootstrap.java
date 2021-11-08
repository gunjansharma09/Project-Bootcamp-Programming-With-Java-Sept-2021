package com.bootcampproject.bootcamp_project.bootstrap;

import com.bootcampproject.bootcamp_project.dto.AddressDto;
import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.service.RoleService;
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
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>>>>>Save boostrap data>>>>");
        String adminEmail = "admin@tothenew.com";
        String customerEmail = "shweta.goel@tothenew.com";
        String sellerEmail = "seller@tothenew.com";
        Role roleAdmin = null;
        Role roleSeller = null;
        Role roleCustomer = null;
        if (!roleService.isRoleExists(RoleEnum.ADMIN.name())) {
            roleAdmin = roleService.saveRole(RoleEnum.ADMIN.name());
        }
        if (!roleService.isRoleExists(RoleEnum.SELLER.name())) {
            roleSeller = roleService.saveRole(RoleEnum.SELLER.name());
        }
        if (!roleService.isRoleExists(RoleEnum.CUSTOMER.name())) {
            roleCustomer = roleService.saveRole(RoleEnum.CUSTOMER.name());
        }
        if (!userService.isUserByEmailExists(adminEmail)) {
            UserDto userDto = new UserDto();
            userDto.setEmail(adminEmail);
            userDto.setFirstName("Admin");
            userDto.setLastName("Admin");
            userDto.setPassword("admin");
            userService.saveAdmin(userDto);
        }

        if (!userService.isUserByEmailExists(customerEmail)) {
            CustomerDto customerDto = new CustomerDto();
            customerDto.setEmail(customerEmail);
            customerDto.setFirstName("Customer");
            customerDto.setLastName("Customer");
            customerDto.setPassword("customer");
            customerDto.setAddress(new ArrayList<AddressDto>());
            userService.saveCustomer(customerDto);
        }

        if (!userService.isUserByEmailExists(sellerEmail)) {
            SellerDto sellerDto = new SellerDto();
            sellerDto.setEmail(sellerEmail);
            sellerDto.setFirstName("Seller");
            sellerDto.setLastName("Seller");
            sellerDto.setPassword("seller");
            sellerDto.setAddress(new AddressDto());
            userService.saveSeller(sellerDto);
        }


//            userService.saveCustomer(CustomerDto.builder()
//                    .firstName("Admin")
//                    .lastName("admin")
//                    .email(adminEmail)
//                   .password("admin")
//                    .build(), roleAdmin);


//        if (!userService.isUserByEmailExists(customerEmail)) {
//            userService.save(UserDto.builder()
//                    .firstName("Customer")
//                    .lastName("customer")
//                    .email(customerEmail)
//                    .password("customer")
//                    .build(), roleCustomer);
//        }
//
//        if (!userService.isUserByEmailExists(sellerEmail)) {
//            userService.save(UserDto.builder()
//                    .firstName("Seller")
//                    .lastName("seller")
//                    .email(sellerEmail)
//                    .password("seller")
//                    .build(), roleSeller);
//        }

        log.info(">>>>>>Done saving boostrap data>>>>");


    }
}





