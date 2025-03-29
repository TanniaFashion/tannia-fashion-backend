package com.tanniafashion.tanniafashion.services;

import java.util.List;
import java.util.Optional;

import com.tanniafashion.tanniafashion.dto.ProductCreateDTO;
import com.tanniafashion.tanniafashion.dto.ProductDTO;
import com.tanniafashion.tanniafashion.dto.ProductUpdateDTO;
import com.tanniafashion.tanniafashion.models.enums.Category;

public interface ProductService {
    List<ProductDTO> getAllProducts();
    Optional<ProductDTO> getProductById(Long id);
    ProductDTO createProduct(ProductCreateDTO productCreateDto);
    ProductDTO updateProduct(Long id, ProductUpdateDTO productUpdateDto);
    void deleteProduct(Long id);
    List<ProductDTO> searchProductsByName(String name);
    List<ProductDTO> searchProductsByCategory(Category category);
}
