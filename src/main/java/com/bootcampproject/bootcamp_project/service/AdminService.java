package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.CustomerResponseDTO;
import com.bootcampproject.bootcamp_project.dto.SellerResponseDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

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


    //--------------------------------PAGINATION STARTS-------------------------------------------------------------------------------------
    public List<SellerResponseDTO> findListOfSellers(Integer pageSize, Integer pageOffset, String sortBy, String email) {
        if (!Objects.isNull(email)) {
            Optional<User> sellerOption = userRepository.findByEmail(email);
            if (sellerOption.isPresent())
                return Collections.singletonList(SellerResponseDTO.mapper(sellerOption.get().getSeller())); //return a list containing single seller object found by email.
            return null;

        }

        Integer pageNumber = 0;
        if (Objects.isNull(pageSize))
            pageSize = 10;
        if (!Objects.isNull(pageOffset) && pageOffset != 0) // starting k records kaha se dikhenege ye pageoffset se pta chalata h.. agar page offset 10 dia h to starting k 10 record dikhai ni denge.. starting k 10 records 0-9 honge.. indexing 0 se start hoti h
            pageNumber = pageOffset / pageSize; // kitne items dikhenge ye page size define krta h
        if (Objects.isNull(sortBy))
            sortBy = "id";
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc(sortBy)));
        List<Seller> sellers = sellerRepository.findAll(pageable).toList();
        List<SellerResponseDTO> sellerResponseDTOS = sellers.stream().map(SellerResponseDTO::mapper).collect(Collectors.toList());
        return sellerResponseDTOS;
    }

    //---------------------------------------------------------------------------------------------------------------------------------------------------------
    // find list of customer
    public List<CustomerResponseDTO> findListOfCustomer(Integer pageSize, Integer pageOffSet, String sortBy, String email) {
        if (!Objects.isNull(email)) {
            Optional<User> customerOption = userRepository.findByEmail(email);
            if (customerOption.isPresent())
                return Collections.singletonList(CustomerResponseDTO.mapper(customerOption.get().getCustomer()));
            return null;
        }

        Integer pageNumber = 0;
        if (Objects.isNull(pageSize))
            pageSize = 10;
        if (Objects.isNull(pageOffSet)) {
            pageOffSet = 0;
        } else {
            //(Objects.isNull(pageOffSet) && pageOffSet != 0)
            pageNumber = pageOffSet / pageSize;
        }

        if (Objects.isNull(sortBy))
            sortBy = "id";
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Order.asc(sortBy)));

        List<Customer> customers = customerRepository.findAll(pageable).toList();
        List<CustomerResponseDTO> customerResponseDTOS = customers.stream().map(CustomerResponseDTO::mapper).collect(Collectors.toList());

        return customerResponseDTOS;
    }
    //------------------------------PAGINATION ENDS----------------------------------------------------------------------------------------

    //------------------------------------------Email--------------------------------------------------------------------------------------
    public void sendEmail(String email, String body) {
        emailService.sendEmailAsync(email, "Hi there!", body);
    }


    //----------------------------------Customer active and deactivate methods start--------------------------------------------------------
    //  --------------activate customer by admin----------------------------
    @Transactional
    public ResponseEntity<?> activateDeactivateCustomerAccountByAdmin(Long id, boolean isActive) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>("Customer not found ,", HttpStatus.NOT_FOUND);
        }
        Customer customer = customerOptional.get();


        // Condition to check if user is deactivated
        if (isActive) {
            if (customer.getUser().getIsActive())
                return new ResponseEntity<>("Customer is already activated!", HttpStatus.OK);
            else {
                customer.getUser().setIsActive(isActive);
                customerRepository.save(customer);
                sendEmail(customer.getUser().getEmail(), "Congratulations! Your account has been activated!");
                return new ResponseEntity<>("Customer activated successfully!", HttpStatus.OK);
            }
        } else {
            if (!customer.getUser().getIsActive())
                return new ResponseEntity<>("Customer is already deactivated!", HttpStatus.OK);
            else {
                customer.getUser().setIsActive(isActive);
                customerRepository.save(customer);
                sendEmail(customer.getUser().getEmail(), "Your account has been deactivated, please contact admin : admin@tothenew.com!" );
                return new ResponseEntity<>("Customer account deactivated Successfully", HttpStatus.OK);
            }
        }
    }

    //--------------------------deactivate customer by admin------------------------------------------------

//    public ResponseEntity<?> deactivateCustomerAccountByAdmin(Long id) {
//        Optional<Customer> customerOptional = customerRepository.findById(id);
//        if (!customerOptional.isPresent()) {
//            return new ResponseEntity<>("No customer exists with this user id! ,", HttpStatus.NOT_FOUND);
//        }
//        Customer customer = customerOptional.get();
//
//
//        if (customer.getUser().getIsActive()) {
//            customer.getUser().setIsActive(false);
//            customerRepository.save(customer);
//            sendEmail(customer.getUser().getEmail(), "Your account has been deactivated, please contact admin!");
//            return new ResponseEntity<>("Customer account deactivated Successfully", HttpStatus.OK);
//        }
//        return new ResponseEntity<>("Customer is already deactivated!", HttpStatus.OK);
//    }

    //--------------------------------------- Customer active and deactivate methods end------------------------------------------------------


    //---------------------------------------Seller active and deactivation methods start-----------------------------------------------------

    @Transactional
    public ResponseEntity<?> activateDeactivateSellerAccountByAdmin(Long id, boolean isActive) {
        Optional<Seller> sellerOptional = sellerRepository.findById(id);

        if (!sellerOptional.isPresent()) {
            return new ResponseEntity<>("No seller exists with this user id!", HttpStatus.NOT_FOUND);
        }
        Seller seller = sellerOptional.get();

        // Condition to check if user is deactivated
        if (isActive) {
            if (seller.getUser().getIsActive() == null || seller.getUser().getIsActive()) {
                return new ResponseEntity<>("Seller is already activated!", HttpStatus.OK);
            } else {
                seller.getUser().setIsActive(isActive);
                sellerRepository.save(seller);
                sendEmail(seller.getUser().getEmail(), "Congratulations! Your account has been activated!");
                return new ResponseEntity<>("Seller activated Successfully", HttpStatus.OK);
            }

        } else {
            if (!seller.getUser().getIsActive()) {

                return new ResponseEntity<>("Seller is already deactivated!", HttpStatus.OK);
            } else {
                seller.getUser().setIsActive(false);
                sellerRepository.save(seller);
                sendEmail(seller.getUser().getEmail(), "Your account has been deactivated, please contact admin!");
                return new ResponseEntity<>("Seller account deactivated Successfully", HttpStatus.OK);
            }


        }
    }

    // -----------------------------------to unlock customer ------------------------------------------------------------------------------------------

    public ResponseEntity<?> isCustomerLocked(Long id, boolean isLocked) {
        Optional<Customer> customerOptional = customerRepository.findById(id);
        if (!customerOptional.isPresent()) {
            return new ResponseEntity<>("Customer not found ,", HttpStatus.NOT_FOUND);
        }
        Customer customer = customerOptional.get();
        if (isLocked) {
            if (!customer.getUser().getIsLocked()) {
                return new ResponseEntity<>("Customer's account is already unlocked!", HttpStatus.OK);
            } else {
                customer.getUser().setIsLocked(isLocked);
                customerRepository.save(customer);
                sendEmail(customer.getUser().getEmail(), "Congratulations! Your account has been unlocked !");
                return new ResponseEntity<>("Customer's account unlocked successfully!", HttpStatus.OK);
            }

        } else {
            if (!customer.getUser().getIsLocked())
                return new ResponseEntity<>("Customer is already locked!", HttpStatus.OK);
            else {
                customer.getUser().setIsLocked(isLocked);
                customerRepository.save(customer);
                sendEmail(customer.getUser().getEmail(), "Your account has been locked, please contact admin!");
                return new ResponseEntity<>("Customer account locked Successfully", HttpStatus.OK);
            }
        }


    }

    //------------------------------------to unlock seller -----------------------------------------------------------------------------------------------

    public ResponseEntity<?> isSellerLocked(Long id, boolean isLocked) {
        Optional<Seller> sellerOptional = sellerRepository.findById(id);

        if (!sellerOptional.isPresent()) {
            return new ResponseEntity<>("No seller exists with this user id!", HttpStatus.NOT_FOUND);
        }
        Seller seller = sellerOptional.get();

        if (isLocked) {
            if (seller.getUser().getIsLocked() == null || seller.getUser().getIsLocked() == false) {
                return new ResponseEntity<>("Seller's account is already unlocked!", HttpStatus.OK);
            } else {
                seller.getUser().setIsLocked(isLocked);
                sellerRepository.save(seller);
                sendEmail(seller.getUser().getEmail(), "Congratulations! Your account has been unlocked!");
                return new ResponseEntity<>("Seller's account has been unlocked Successfully", HttpStatus.OK);
            }
        }
        else {
            if (!seller.getUser().getIsLocked())
                return new ResponseEntity<>("Seller is already locked!", HttpStatus.OK);
            else {
                seller.getUser().setIsLocked(isLocked);
                sellerRepository.save(seller);
                sendEmail(seller.getUser().getEmail(), "Your account has been locked, please contact admin!");
                return new ResponseEntity<>("Seller account locked Successfully", HttpStatus.OK);
            }
        }


    }
//    public String deactivateSellerAccountByAdmin(Long id) {
//        Optional<Seller> sellerOptional = sellerRepository.findById(id);
//        if (!sellerOptional.isPresent()) {
//            return "No seller exists with this user id!";
//        }
//        Seller seller = sellerOptional.get();
//
//        if (seller.getUser().getIsActive()) {
//            seller.getUser().setIsActive(false);
//            sellerRepository.save(seller);
//            sendEmail(seller.getUser().getEmail(), "Your account has been deactivated, please contact admin!");
//            return "Seller account deactivated Successfully";
//        }
//        return "Seller is already deactivated!";
//    }


}
