package com.example.flowerShop.utils.shoppingCart;

import com.example.flowerShop.dto.order.OrderDetailedDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.entity.Order;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.ShoppingCart;
import com.example.flowerShop.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class ShoppingCartUtils {

    public boolean validateCartMap(ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        return  !Objects.equals(shoppingCartDetailedDTO.getId_user(), null);
    }

    public static void updateCartValues(ShoppingCart shoppingCart, ShoppingCartDetailedDTO shoppingCartDetailedDTO, List<OrderItem> orderItemList) {

        if (Objects.nonNull(shoppingCartDetailedDTO.getTotalPrice())) {
            shoppingCart.setTotalPrice(shoppingCartDetailedDTO.getTotalPrice());
        }
        if (Objects.nonNull(shoppingCartDetailedDTO.getId_orderItems())) {
            shoppingCart.setOrderItems(orderItemList);
        }
    }
}
