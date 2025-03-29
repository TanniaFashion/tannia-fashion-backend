package com.tanniafashion.tanniafashion.dto;

import java.math.BigDecimal;

import com.tanniafashion.tanniafashion.models.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stock;
    private Category category;
    private String brand;
    private String imgUrl;
}
