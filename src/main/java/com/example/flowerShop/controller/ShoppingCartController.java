package com.example.flowerShop.controller;

import com.example.flowerShop.dto.shoppingCart.ShoppingCartDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.ShoppingCartServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
@CrossOrigin("*")
public class ShoppingCartController {

    private final ShoppingCartServiceImpl shoppingCartService;

    @Autowired
    private HttpSession session;

    @Autowired
    public ShoppingCartController(ShoppingCartServiceImpl shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }

    @GetMapping("cart/get/all")
    public ResponseEntity<List<ShoppingCartDTO>> getAllCarts() {
        return this.shoppingCartService.getAllCarts();
    }

    @GetMapping("cart/getByUser/{id}")
    public ResponseEntity<ShoppingCartDTO> getCartByUserId(@PathVariable UUID id) {
        return this.shoppingCartService.getCartByUserId(id);
    }

    @GetMapping("/home")
    public ModelAndView createCart() {
        ModelAndView modelAndView = new ModelAndView("home");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        if (currentUser.getId() != null) {
            modelAndView.addObject("user", currentUser);
        }
        return modelAndView;
    }

    @PostMapping("cart/add")
    public RedirectView addCart(@RequestBody ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        ResponseEntity<String> response = this.shoppingCartService.addCart(shoppingCartDetailedDTO);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            return new RedirectView("/product/listOfProducts");
        }
        return new RedirectView("/userProfile");
    }

    @PutMapping("cart/update/{id}")
    public ResponseEntity<String> updateCartByID(@PathVariable UUID id, @RequestBody ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        return this.shoppingCartService.updateCartById(id, shoppingCartDetailedDTO);
    }

    @DeleteMapping("cart/delete/{id}")
    public ResponseEntity<String> deleteCartByID(@PathVariable UUID id) {
        return this.shoppingCartService.deleteCartById(id);
    }
}
