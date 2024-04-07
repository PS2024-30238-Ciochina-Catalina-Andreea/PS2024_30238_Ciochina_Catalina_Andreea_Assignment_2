package com.example.flowerShop.dto.orderItem;

import com.example.flowerShop.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class OrderItemDTO {

    private UUID id;
    private int quantity;
    private Product product;
}
