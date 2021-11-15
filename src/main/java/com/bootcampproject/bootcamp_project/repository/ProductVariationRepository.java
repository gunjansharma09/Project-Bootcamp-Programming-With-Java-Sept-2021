package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.Product;
import com.bootcampproject.bootcamp_project.entity.ProductVariation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductVariationRepository extends CrudRepository<ProductVariation, Long> {


  List<Object> findByProductId(Pageable paging, Product id);

  List<ProductVariation> findByProductId(Product id);

  @Query(value = "select pv.id, "
      + "pv.is_active,pv.metadata,pv.price,pv.primary_image_name,pv.quantity_available,pv.secondary_image_name "
      + " from product_variation pv"
      + " Join"
      + " product p"
      + " on p.id = pv.product_id "
      + "where p.id=:productId", nativeQuery = true)
  List<Object> findVariationsByProductId(Long productId, Pageable pageable);
}
