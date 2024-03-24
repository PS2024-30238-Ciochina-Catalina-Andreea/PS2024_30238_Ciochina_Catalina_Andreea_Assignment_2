package com.example.flowerShop.utils.order;

import com.example.flowerShop.dto.orderItem.OrderItemDTO;
import com.example.flowerShop.dto.orderItem.OrderItemDetailedDTO;
import com.example.flowerShop.entity.OrderItem;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class OrderItemUtils {

    /**
     * Validation of the quantity and product to not be empty
     * @param orderItemDetailedDTO
     * @return
     */
    public boolean validateOrderItemMap(OrderItemDetailedDTO orderItemDetailedDTO) {
        return !Objects.equals(orderItemDetailedDTO.getQuantity(), null)
                && !Objects.equals(orderItemDetailedDTO.getId_product(), "");
    }

    /**
     * Updates the entry of an existing order item from DB, updates content of the Product and its Quantity
     * @param orderItemExisting
     * @param orderItemDTO
     */
    public static void updateOrderItemsValues(OrderItem orderItemExisting, OrderItemDTO orderItemDTO) {
        if (Objects.nonNull(orderItemDTO.getProduct())) {
            orderItemExisting.setProduct(orderItemDTO.getProduct());
        }
        if (Objects.nonNull(orderItemDTO.getQuantity())) {
            orderItemExisting.setQuantity(orderItemDTO.getQuantity());
        }
    }
}
