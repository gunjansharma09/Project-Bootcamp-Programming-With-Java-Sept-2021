package com.bootcampproject.bootcamp_project.dto;

import com.bootcampproject.bootcamp_project.entity.Seller;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;

@EqualsAndHashCode(callSuper = true)
@Data

public class SellerResponseDTO extends UserResponseDTO {
    private String gst;
    private String companyContact;
    @Column(unique = true)
    private String companyName;

    public static SellerResponseDTO mapper(Seller seller) {
        SellerResponseDTO sellerResponseDTO = new SellerResponseDTO();
        sellerResponseDTO.setFirstName(seller.getUser().getFirstName());
        sellerResponseDTO.setMiddleName(seller.getUser().getMiddleName());
        sellerResponseDTO.setLastName(seller.getUser().getLastName());
        sellerResponseDTO.setEmail(seller.getUser().getEmail());
        sellerResponseDTO.setActive(seller.getUser().getIsActive());
        sellerResponseDTO.setId(seller.getUser().getId());
        sellerResponseDTO.setLocked(seller.getUser().getIsLocked());
        sellerResponseDTO.setCompanyContact(seller.getCompanyContact());
        sellerResponseDTO.setCompanyName(seller.getCompanyName());
        sellerResponseDTO.setGst(seller.getGst());
        return sellerResponseDTO;
    }
}
