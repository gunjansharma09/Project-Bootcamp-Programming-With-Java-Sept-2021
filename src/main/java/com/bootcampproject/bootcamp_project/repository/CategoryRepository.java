package com.bootcampproject.bootcamp_project.repository;

import com.bootcampproject.bootcamp_project.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    public Optional<Category> findByName(String name);

    public List<Category> findByParentCategory(Long id);


//    @Query(value =
//            "select id, name from category where parent_id=:value", nativeQuery = true)
//    List<Map<Object, Object>> findByParentCategory(Long value);


    @Query(value = "select id,name " +
            "from category " +
            "where parent_id is null", nativeQuery = true)
    List<Category> findByCategoryIsNull();

    List<Category> findByParentCategoryNotNull();
}
