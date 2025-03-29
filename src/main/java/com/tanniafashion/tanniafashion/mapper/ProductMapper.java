package com.tanniafashion.tanniafashion.mapper;

import org.springframework.stereotype.Component;

import com.tanniafashion.tanniafashion.dto.ProductCreateDTO;
import com.tanniafashion.tanniafashion.dto.ProductDTO;
import com.tanniafashion.tanniafashion.dto.ProductUpdateDTO;
import com.tanniafashion.tanniafashion.models.Product;

@Component
public class ProductMapper {

    public ProductDTO toDTO(Product product) {
        return new ProductDTO(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getPrice(),
            product.getStock(),
            product.getCategory(),
            product.getBrand(),
            product.getImgUrl()
        );
    }

    public Product toEntity(ProductCreateDTO dto) {
        System.out.println("Mapping ProductCreateDTO: " + dto);

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setImgUrl(dto.getImgUrl());

        System.out.println("Mapped Product Entity: " + product);
        return product;
    }

    public void updateEntityFromDto(ProductUpdateDTO dto, Product product) {
        if (dto.getName() != null) { product.setName(dto.getName()); }
        if (dto.getDescription() != null) { product.setDescription(dto.getDescription()); }
        if (dto.getPrice() != null) { product.setPrice(dto.getPrice()); }
        if (dto.getStock() != null) { product.setStock(dto.getStock()); }
        if (dto.getCategory() != null) { product.setCategory(dto.getCategory()); }
        if (dto.getBrand() != null) { product.setBrand(dto.getBrand()); }
        if (dto.getImgUrl() != null) { product.setImgUrl(dto.getImgUrl()); }
    }

}
