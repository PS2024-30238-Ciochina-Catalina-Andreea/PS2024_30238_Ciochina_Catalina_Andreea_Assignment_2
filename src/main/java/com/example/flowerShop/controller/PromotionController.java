package com.example.flowerShop.controller;

import com.example.flowerShop.dto.promotion.PromotionDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.service.impl.PromotionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/promotion")
@CrossOrigin("*")
public class PromotionController {

    private final PromotionServiceImpl promotionService;

    @Autowired
    public PromotionController(PromotionServiceImpl promotionService){
        this.promotionService = promotionService;
    }

    @GetMapping("/get/all")
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        return this.promotionService.getAllPromotions();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable UUID id) {
        return this.promotionService.getPromotionById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addPromotion(@RequestBody PromotionDetailedDTO promotionDetailedDTO) {
        return this.promotionService.addPromotion(promotionDetailedDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePromotionById(@PathVariable UUID id, @RequestBody PromotionDetailedDTO promotionDetailedDTO) {
        return this.promotionService.updatePromotionById(id, promotionDetailedDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePromotionById(@PathVariable UUID id) {
        return this.promotionService.deletePromotionById(id);
    }
}
