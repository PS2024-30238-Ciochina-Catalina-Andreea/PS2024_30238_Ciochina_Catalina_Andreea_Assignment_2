package com.example.flowerShop.dto.product;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ProductDetailedDTO {

    private UUID id;
    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String category;
}
