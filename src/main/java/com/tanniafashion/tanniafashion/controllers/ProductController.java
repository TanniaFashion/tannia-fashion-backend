package com.tanniafashion.tanniafashion.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tanniafashion.tanniafashion.dto.ProductCreateDTO;
import com.tanniafashion.tanniafashion.dto.ProductDTO;
import com.tanniafashion.tanniafashion.dto.ProductUpdateDTO;
import com.tanniafashion.tanniafashion.models.enums.Category;
import com.tanniafashion.tanniafashion.services.ProductService;

import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product Controller", description = "Product Operations")
public class ProductController {

    @Autowired
    private ProductService productService;


    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductCreateDTO productCreateDTO) {
        ProductDTO createdProduct = productService.createProduct(productCreateDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts();
        
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Optional<ProductDTO> productDTO = productService.getProductById(id);

        if (productDTO.isPresent()) {
            return ResponseEntity.ok(productDTO.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("message", "Product not found"));
        }           
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchProductsByName(@RequestParam String name) {
        List<ProductDTO> products = productService.searchProductsByName(name);

        if(products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No products found");
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> searchProductsByCategory(@PathVariable Category category) {
        List<ProductDTO> products = productService.searchProductsByCategory(category);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("No products found");
        }

        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody ProductUpdateDTO productUpdateDTO) {
        try {
            ProductDTO updatedProduct = productService.updateProduct(id, productUpdateDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (RuntimeException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
        }
    }
}
