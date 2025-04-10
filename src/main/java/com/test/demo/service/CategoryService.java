package com.test.demo.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.test.demo.entity.Category;

public interface CategoryService {
    Page<Category> getAllCategories(Pageable pageable);
    Category getCategoryById(Long id);
    Category createCategory(Category category);
    Category updateCategory(Long id, Category category);
    void deleteCategory(Long id);
}
