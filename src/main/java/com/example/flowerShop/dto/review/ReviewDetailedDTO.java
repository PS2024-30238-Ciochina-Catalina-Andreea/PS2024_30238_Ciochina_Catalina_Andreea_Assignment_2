package com.example.flowerShop.dto.review;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ReviewDetailedDTO {

    private UUID id;
    private UUID id_product;
    private UUID id_user;
    private String text;
    private int rating;
}
