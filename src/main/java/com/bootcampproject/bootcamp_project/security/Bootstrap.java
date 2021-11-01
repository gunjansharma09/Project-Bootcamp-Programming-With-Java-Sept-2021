package com.bootcampproject.bootcamp_project.security;

import com.bootcampproject.bootcamp_project.dto.UserDto;
import com.bootcampproject.bootcamp_project.entity.Role;
import com.bootcampproject.bootcamp_project.enums.RoleEnum;
import com.bootcampproject.bootcamp_project.service.RoleService;
import com.bootcampproject.bootcamp_project.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@Data
@Slf4j
@AllArgsConstructor
public class Bootstrap implements ApplicationRunner {
    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info(">>>>>>Save boostrap data>>>>");
        String adminEmail = "admin@tothenew.com";
        String customerEmail = "customer@tothenew.com";
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
            userService.save(UserDto.builder()
                    .firstName("Admin")
                    .lastName("admin")
                    .email(adminEmail)
                    .password("admin")
                    .build(), roleAdmin);
        }

        if (!userService.isUserByEmailExists(customerEmail)) {
            userService.save(UserDto.builder()
                    .firstName("Customer")
                    .lastName("customer")
                    .email(customerEmail)
                    .password("customer")
                    .build(), roleCustomer);
        }

        if (!userService.isUserByEmailExists(sellerEmail)) {
            userService.save(UserDto.builder()
                    .firstName("Seller")
                    .lastName("seller")
                    .email(sellerEmail)
                    .password("seller")
                    .build(), roleSeller);
        }

        log.info(">>>>>>Done saving boostrap data>>>>");


    }
}





