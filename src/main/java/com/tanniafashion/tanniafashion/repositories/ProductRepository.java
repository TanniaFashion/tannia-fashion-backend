package com.tanniafashion.tanniafashion.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tanniafashion.tanniafashion.models.Product;
import com.tanniafashion.tanniafashion.models.enums.Category;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>{
    List<Product> findByCategory(Category category);
    List<Product> findByBrand(String brand);
    List<Product> findByNameContainingIgnoreCase(String name);
} 