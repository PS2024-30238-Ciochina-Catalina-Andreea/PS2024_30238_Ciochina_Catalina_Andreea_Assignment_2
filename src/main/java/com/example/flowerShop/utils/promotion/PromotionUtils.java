package com.example.flowerShop.utils.promotion;

import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.Promotion;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@NoArgsConstructor
public class PromotionUtils {

    public boolean validatePromotionMap(PromotionDetailedDTO promotionDetailedDTO) {
        return  !Objects.equals(promotionDetailedDTO.getName(), null)
                && !Objects.equals(promotionDetailedDTO.getDiscountPercentage(), null) ;
    }

    public static void updatePromotion(Promotion promotion, PromotionDetailedDTO promotionDetailedDTO, List<Product> products) {

        if (Objects.nonNull(promotionDetailedDTO.getId_products())) {
            promotion.setProducts(products);
        }
        if (Objects.nonNull(promotionDetailedDTO.getName())) {
            promotion.setName(promotionDetailedDTO.getName());
        }
        if (Objects.nonNull(promotionDetailedDTO.getDiscountPercentage())) {
            promotion.setDiscountPercentage(promotionDetailedDTO.getDiscountPercentage());
        }
    }
}
