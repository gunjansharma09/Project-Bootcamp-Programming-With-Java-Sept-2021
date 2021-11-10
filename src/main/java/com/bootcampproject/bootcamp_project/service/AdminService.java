package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.entity.Customer;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.entity.User;
import com.bootcampproject.bootcamp_project.repository.CustomerRepository;
import com.bootcampproject.bootcamp_project.repository.SellerRepository;
import com.bootcampproject.bootcamp_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private EmailService emailService;


    //PAGINATION STARTS
    public List<Seller> findListOfSellers(Integer pageSize, Integer pageOffset, String sortBy, String email) {
        if (!Objects.isNull(email)) {
            Optional<User> sellerOption = userRepository.findByEmail(email);
            if (sellerOption.isPresent())
                return Collections.singletonList(sellerOption.get().getSeller()); //return a list containing single seller object found by email.
            return null;

        }

        Integer pageNumber = 0;
        if (Objects.isNull(pageSize))
            pageSize = 10;
        if (Objects.isNull(pageOffset) && pageOffset != 0) // starting k records kaha se dikhenege ye pageoffset se pta chalata h.. agar page offset 10 dia h to starting k 10 record dikhai ni denge.. starting k 10 records 0-9 honge.. indexing 0 se start hoti h
            pageNumber = pageOffset / pageSize; // kitne items dikhenge ye page size define krta h
        if (Objects.isNull(sortBy))
            sortBy = "id";
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc(sortBy)));
        return sellerRepository.findAll(pageable).toList();
    }

    public List<Customer> findListOfCustomer(Integer pageSize, Integer pageOffSet, String sortBy, String email) {
        if (!Objects.isNull(email)) {
            Optional<User> customerOption = userRepository.findByEmail(email);
            if (customerOption.isPresent())
                return Collections.singletonList(customerOption.get().getCustomer());
            return null;
        }

        Integer pageNumber = 0;
        if (Objects.isNull(pageSize))
            pageSize = 10;
        if (Objects.isNull(pageOffSet) && pageOffSet != 0)
            pageNumber = pageOffSet / pageSize;
        if (Objects.isNull(sortBy))
            sortBy = "id";
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc(sortBy)));
        return customerRepository.findAll(pageable).toList();
    }
    //PAGINATION ENDS

    //Email 
    public void sendEmail(String email, String body) {
        emailService.sendSimpleMessage(email, "Hi there!", body);
    }

    //Customer active and deactivate methods start
    @Transactional
    public String activateCustomerAccountByAdmin(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            return "No customer exists with this user id!";
        }
        Customer customer = customerOptional.get();


        // Condition to check if user is deactivated
        if (!customer.getUser().getIsActive()) {
            customer.getUser().setIsActive(true);
            customerRepository.save(customer);
            sendEmail(customer.getUser().getEmail(), "Congratulations! Your account has been activated!");
            return "Customer activated Successfully";
        }
        return "Customer is already activated!";
    }

    public String deactivateCustomerAccountByAdmin(Long id) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            return "No customer exists with this user id!";
        }
        Customer customer = customerOptional.get();


        if (customer.getUser().getIsActive()) {
            customer.getUser().setIsActive(false);
            customerRepository.save(customer);
            sendEmail(customer.getUser().getEmail(), "Your account has been deactivated, please contact admin!");
            return "Customer account deactivated Successfully";
        }
        return "Customer is already deactivated!";
    }

    // Customer active and deactivate methods end


    //Seller active and deactivation methods start

    @Transactional
    public String activateSellerAccountByAdmin(Long id) {
        Optional<Seller> sellerOptional = sellerRepository.findById(id);

        if (!sellerOptional.isPresent()) {
            return "No seller exists with this user id!";
        }
        Seller seller = sellerOptional.get();

        // Condition to check if user is deactivated
        if (seller.getUser().getIsActive() == null || seller.getUser().getIsActive() == false) {
            seller.getUser().setIsActive(true);
            sellerRepository.save(seller);
            sendEmail(seller.getUser().getEmail(), "Congratulations! Your account has been activated!");
            return "Seller activated Successfully";
        }
        return "Seller is already activated!";
    }

    public String deactivateSellerAccountByAdmin(Long id) {
        Optional<Seller> sellerOptional = sellerRepository.findById(id);
        if (!sellerOptional.isPresent()) {
            return "No seller exists with this user id!";
        }
        Seller seller = sellerOptional.get();

        if (seller.getUser().getIsActive()) {
            seller.getUser().setIsActive(false);
            sellerRepository.save(seller);
            sendEmail(seller.getUser().getEmail(), "Your account has been deactivated, please contact admin!");
            return "Seller account deactivated Successfully";
        }
        return "Seller is already deactivated!";
    }


}
