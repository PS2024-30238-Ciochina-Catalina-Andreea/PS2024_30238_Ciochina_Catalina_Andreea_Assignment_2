package com.example.flowerShop.utils.category;

import com.example.flowerShop.dto.category.CategoryDetailedDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.entity.Category;
import com.example.flowerShop.entity.Product;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class CategoryUtils {

    /**
     * Validates the category name and description for creating category
     * @param categoryDetailedDTO
     * @return
     */
    public boolean validateCategoryMap(CategoryDetailedDTO categoryDetailedDTO) {
        return !Objects.equals(categoryDetailedDTO.getName(), "")
                && !Objects.equals(categoryDetailedDTO.getDescription(), "");
    }

    /**
     * Updates category's description and list of products
     * @param categoryExisting
     * @param category
     */
    public static void updateCategoryValues(Category categoryExisting, CategoryDetailedDTO category) {
        if (Objects.nonNull(category.getDescription()) && !"".equalsIgnoreCase(category.getDescription())) {
            categoryExisting.setDescription(category.getDescription());
        }
    }
}