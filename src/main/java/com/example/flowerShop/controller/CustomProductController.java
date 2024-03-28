package com.example.flowerShop.controller;

import com.example.flowerShop.dto.customProduct.CustomProductDTO;
import com.example.flowerShop.dto.customProduct.CustomProductDetailedDTO;
import com.example.flowerShop.service.impl.CustomProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/bouquet")
@CrossOrigin("*")
public class CustomProductController {

    private final CustomProductServiceImpl customProductService;

    @Autowired
    public CustomProductController(CustomProductServiceImpl customProductService){
        this.customProductService = customProductService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<CustomProductDTO>> getAllCustomProducts() {
        return this.customProductService.getAllCustomProducts();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<CustomProductDTO> getCustomProductById(@PathVariable UUID id) {
        return this.customProductService.getCustomProductById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addCustomProduct(@RequestBody CustomProductDetailedDTO productDetailedDTO) {
        return this.customProductService.addCustomProduct(productDetailedDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCustomProductById(@PathVariable UUID id) {
        return this.customProductService.deleteCustomProductById(id);
    }
}
