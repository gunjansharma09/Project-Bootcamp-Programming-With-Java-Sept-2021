package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.Seller;
import com.bootcampproject.bootcamp_project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
    Long countByGst(String gst);

}
