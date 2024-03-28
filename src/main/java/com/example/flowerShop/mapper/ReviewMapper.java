package com.example.flowerShop.mapper;

import com.example.flowerShop.dto.review.ReviewDTO;
import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.Review;
import com.example.flowerShop.entity.User;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ReviewMapper implements Mapper<Review, ReviewDTO, ReviewDetailedDTO> {

    @Override
    public ReviewDetailedDTO convertToDTO(Review review) {
        if (review != null) {
            return ReviewDetailedDTO.builder()
                    .id(review.getId())
                    .text(review.getText())
                    .id_user(review.getUser().getId())
                    .id_product(review.getProduct().getId())
                    .rating(review.getRating())
                    .build();
        }
        return null;
    }

    @Override
    public Review convertToEntity(ReviewDTO reviewDTO) {
        if (reviewDTO != null) {
            return Review.builder()
                    .id(reviewDTO.getId())
                    .text(reviewDTO.getText())
                    .user(reviewDTO.getUser())
                    .product(reviewDTO.getProduct())
                    .rating(reviewDTO.getRating())
                    .build();
        }
        return null;
    }

    public ReviewDTO convToReviewWithUserAndProduct(ReviewDetailedDTO reviewDetailedDTO, Optional<User> user, Optional<Product> product) {

        if (reviewDetailedDTO != null) {
            return ReviewDTO.builder()
                    .id(reviewDetailedDTO.getId())
                    .text(reviewDetailedDTO.getText())
                    .user(user.get())
                    .product(product.get())
                    .rating(reviewDetailedDTO.getRating())
                    .build();
        }
        return null;
    }
}
