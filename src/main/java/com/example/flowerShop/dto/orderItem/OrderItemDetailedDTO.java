package com.example.flowerShop.dto.orderItem;

import com.example.flowerShop.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class OrderItemDetailedDTO {

    private UUID id;
    private Long quantity;
    private UUID id_product;
    private Order order;
}
