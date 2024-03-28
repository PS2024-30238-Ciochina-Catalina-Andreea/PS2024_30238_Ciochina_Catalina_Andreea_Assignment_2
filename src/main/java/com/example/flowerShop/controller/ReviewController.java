package com.example.flowerShop.controller;

import com.example.flowerShop.dto.review.ReviewDTO;
import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.service.impl.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/review")
@CrossOrigin("*")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewService){
        this.reviewService = reviewService;
    }

    @GetMapping("/getAllByProduct/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByProductId() {
        return this.reviewService.getAllReviewsByProductId();
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable UUID id) {
        return this.reviewService.getReviewById(id);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addReview(@RequestBody ReviewDetailedDTO detailedDTO) {
        return this.reviewService.addReview(detailedDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateReviewById(@PathVariable UUID id, @RequestBody ReviewDetailedDTO reviewDetailedDTO) {
        return this.reviewService.updateReviewById(id, reviewDetailedDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable UUID id) {
        return this.reviewService.deleteReviewById(id);
    }
}
