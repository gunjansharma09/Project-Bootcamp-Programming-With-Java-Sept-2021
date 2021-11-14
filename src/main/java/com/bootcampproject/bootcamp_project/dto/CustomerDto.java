package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Cart;
import com.bootcampproject.bootcamp_project.entity.Orders;
import com.bootcampproject.bootcamp_project.entity.ProductReview;
import com.bootcampproject.bootcamp_project.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder

public class CustomerDto extends UserDto {
    @Pattern(regexp = "(^$|[0-9]{10})",message = "Contact number must contain numeric value and must have 10 digits!!")

    private String contact;
    private List<AddressDto> address;
}
