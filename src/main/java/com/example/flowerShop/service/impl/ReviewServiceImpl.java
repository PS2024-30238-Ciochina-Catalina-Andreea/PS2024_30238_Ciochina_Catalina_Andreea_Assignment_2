package com.example.flowerShop.service.impl;

import com.example.flowerShop.dto.review.ReviewDTO;
import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.mapper.ReviewMapper;
import com.example.flowerShop.repository.ProductRepository;
import com.example.flowerShop.repository.ReviewRepository;
import com.example.flowerShop.repository.UserRepository;
import com.example.flowerShop.service.ReviewService;
import com.example.flowerShop.utils.review.ReviewUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ReviewUtils reviewUtils;

    @Autowired
    public ReviewServiceImpl(UserRepository userRepository,
                             ProductRepository productRepository,
                             ReviewRepository reviewRepository,
                             ReviewMapper reviewMapper,
                             ReviewUtils reviewUtils){
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.reviewUtils = reviewUtils;
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByProductId() {
        return null;
    }

    @Override
    public ResponseEntity<ReviewDTO> getReviewById(UUID id) {
        return null;
    }

    @Override
    public ResponseEntity<String> addReview(ReviewDetailedDTO detailedDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> updateReviewById(UUID id, ReviewDetailedDTO reviewDetailedDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> deleteReviewById(UUID id) {
        return null;
    }
}
