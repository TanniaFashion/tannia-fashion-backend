package com.tanniafashion.tanniafashion.services.implementation;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tanniafashion.tanniafashion.dto.ProductCreateDTO;
import com.tanniafashion.tanniafashion.dto.ProductDTO;
import com.tanniafashion.tanniafashion.dto.ProductUpdateDTO;
import com.tanniafashion.tanniafashion.mapper.ProductMapper;
import com.tanniafashion.tanniafashion.models.Product;
import com.tanniafashion.tanniafashion.models.enums.Category;
import com.tanniafashion.tanniafashion.repositories.ProductRepository;
import com.tanniafashion.tanniafashion.services.ProductService;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDTO> getProductById(Long id) {
        return productRepository.findById(id)
            .map(productMapper::toDTO);
    }

    @Override
    public ProductDTO createProduct(ProductCreateDTO productCreateDto) {
        Product product = productMapper.toEntity(productCreateDto);
        System.out.println("Mapped Product Entity: " + product);
        
        Product savedProduct = productRepository.save(product);
        return productMapper.toDTO(savedProduct);
    }

    @Override
    public ProductDTO updateProduct(Long id, ProductUpdateDTO productUpdateDto) {
        return productRepository.findById(id)
            .map(product -> {
                productMapper.updateEntityFromDto(productUpdateDto, product);

                Product updatedProduct = productRepository.save(product);
                return productMapper.toDTO(updatedProduct);
            }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public List<ProductDTO> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
            .map(productMapper::toDTO)
            .collect(Collectors.toList());
    }

    @Override
    public List<ProductDTO> searchProductsByCategory(Category category) {
        return productRepository.findByCategory(category).stream()
                .map(productMapper::toDTO)
                .collect(Collectors.toList());
    }
    
}
