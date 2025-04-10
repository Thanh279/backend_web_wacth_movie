package com.test.demo.reponsitory;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.demo.entity.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}