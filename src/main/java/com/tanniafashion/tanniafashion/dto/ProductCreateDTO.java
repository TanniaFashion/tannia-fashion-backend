package com.tanniafashion.tanniafashion.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tanniafashion.tanniafashion.models.enums.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductCreateDTO {
    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("price")
    private BigDecimal price;

    @JsonProperty("stock")
    private Integer stock;

    @JsonProperty("category")
    private Category category;

    @JsonProperty("brand")
    private String brand;
    
    @JsonProperty("imgUrl")
    private String imgUrl;
}
