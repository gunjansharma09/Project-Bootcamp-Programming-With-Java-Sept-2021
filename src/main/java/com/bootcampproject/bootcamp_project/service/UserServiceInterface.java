package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.CustomerDto;
import com.bootcampproject.bootcamp_project.dto.SellerDto;

public interface UserServiceInterface {
    public Boolean saveCustomer(CustomerDto customerDto);

    public Boolean saveSeller(SellerDto sellerDto);
}
