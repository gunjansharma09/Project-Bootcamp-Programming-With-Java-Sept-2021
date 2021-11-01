package com.bootcampproject.bootcamp_project.service;

import com.bootcampproject.bootcamp_project.dto.SellerDto;
import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.repository.SellerRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Data
@RequiredArgsConstructor
public class SellerService {
    @Autowired
    private SellerRepository sellerRepository;

    @Transactional
    public Seller save(SellerDto sellerDto) {
        Seller seller = Seller.builder()
                .gst(sellerDto.getGst())
                .companyContact((sellerDto.getCompanyContact()))
                .companyName(sellerDto.getCompanyName())
                .build();
        return sellerRepository.save(seller);
    }

    @Transactional
    public String deleteById(Long id) {
        {
            sellerRepository.deleteById(id);
            return "success";
        }
    }

}
