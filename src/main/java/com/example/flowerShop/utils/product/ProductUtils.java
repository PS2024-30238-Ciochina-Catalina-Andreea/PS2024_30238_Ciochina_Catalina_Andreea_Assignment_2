package com.example.flowerShop.utils.product;

import com.example.flowerShop.dto.product.ProductDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.entity.Product;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class ProductUtils {

    public boolean validateProductMap(ProductDetailedDTO productDetailedDTO) {
        return !Objects.equals(productDetailedDTO.getName(), "")
                && !Objects.equals(productDetailedDTO.getStock(), null)
                && !Objects.equals(productDetailedDTO.getCategory(), null)
                && !Objects.equals(productDetailedDTO.getPrice(), null)
                && !Objects.equals(productDetailedDTO.getDescription(), "");
    }

    public static void updateProductValues(Product productExisting, ProductDTO product) {
        if (Objects.nonNull(product.getName()) && !"".equalsIgnoreCase(product.getName())) {
            productExisting.setName(product.getName());
        }
        if (Objects.nonNull(product.getDescription()) && !"".equalsIgnoreCase(product.getDescription())) {
            productExisting.setDescription(product.getDescription());
        }
        if (Objects.nonNull(product.getPrice()) && product.getPrice() >= 0) {
            productExisting.setPrice(product.getPrice());
        }
        if (Objects.nonNull(product.getStock()) && product.getStock() >= 0) {
            productExisting.setStock(product.getStock());
        }
        if (Objects.nonNull(product.getCategory())) {
            productExisting.setCategory(product.getCategory());
        }
    }
}
