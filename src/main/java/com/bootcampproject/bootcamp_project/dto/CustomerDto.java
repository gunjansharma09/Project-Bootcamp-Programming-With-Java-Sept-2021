package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Cart;
import com.bootcampproject.bootcamp_project.entity.Orders;
import com.bootcampproject.bootcamp_project.entity.ProductReview;
import com.bootcampproject.bootcamp_project.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
public class CustomerDto extends UserDto {
//    private String email;
//    private String firstName;
//    private String middleName;
//    private String lastName;
//    private String password;
//    private String confirmPassword;
    private String contact;
    private List<AddressDto> address;
}
