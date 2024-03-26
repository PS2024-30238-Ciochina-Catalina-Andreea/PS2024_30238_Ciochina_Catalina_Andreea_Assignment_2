package com.example.flowerShop.service;

import com.example.flowerShop.dto.shoppingCart.ShoppingCartDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ShoppingCartService {

    ResponseEntity<List<ShoppingCartDTO>> getAllCarts();

    ResponseEntity<ShoppingCartDTO> getCartById(UUID id);

    ResponseEntity<String> addCart(ShoppingCartDetailedDTO shoppingCartDetailedDTO);

    ResponseEntity<String> updateCartById(UUID id, ShoppingCartDetailedDTO shoppingCartDetailedDTO);

    ResponseEntity<String> deleteCartById(UUID id);
}
