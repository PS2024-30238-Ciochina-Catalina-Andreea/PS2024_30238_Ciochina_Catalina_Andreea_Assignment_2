package com.example.flowerShop.service;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    ResponseEntity<List<ProductDetailedDTO>> getAllProducts();

    ResponseEntity<ProductDetailedDTO> getProductById(UUID id);

    ResponseEntity<String> addProduct(ProductDetailedDTO productDetailedDTO);

    ResponseEntity<String> updateProductById(UUID id, ProductDetailedDTO productDetailedDTO);

    ResponseEntity<String> deleteProductById(UUID id);
}
