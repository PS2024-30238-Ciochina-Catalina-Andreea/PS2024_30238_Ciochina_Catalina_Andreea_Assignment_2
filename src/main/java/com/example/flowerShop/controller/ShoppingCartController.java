package com.example.flowerShop.controller;

import com.example.flowerShop.dto.shoppingCart.ShoppingCartDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.service.impl.ShoppingCartServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cart")
@CrossOrigin("*")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    public ShoppingCartController(ShoppingCartServiceImpl shoppingCartService){
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<ShoppingCartDTO>> getAllCarts(){
        return this.shoppingCartService.getAllCarts();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ShoppingCartDTO> getCartById(@PathVariable UUID id){
        return this.shoppingCartService.getCartById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCart(@RequestBody ShoppingCartDetailedDTO shoppingCartDetailedDTO){
        return this.shoppingCartService.addCart(shoppingCartDetailedDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateCartByID(@PathVariable UUID id, @RequestBody ShoppingCartDetailedDTO shoppingCartDetailedDTO){
        return this.shoppingCartService.updateCartById(id,shoppingCartDetailedDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCartByID(@PathVariable UUID id){
        return this.shoppingCartService.deleteCartById(id);
    }
}
