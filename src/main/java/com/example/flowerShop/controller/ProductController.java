package com.example.flowerShop.controller;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.service.impl.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    /**
     * Injected constructor
     * @param productServiceImpl
     */
    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    /**
     * Gets list of products from db
     * @return ResponseEntity<List<ProductDetailedDTO>>
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<ProductDetailedDTO>> getAllProducts() {
        LOGGER.info("Request for list of products");
        return this.productServiceImpl.getAllProducts();
    }

    /**
     * Gets product by id
     * @param id
     * @return ResponseEntity<ProductDetailedDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDetailedDTO> getProductById(@PathVariable UUID id) {
        LOGGER.info("Request for product by id");
        return this.productServiceImpl.getProductById(id);
    }

    /**
     * Creates a new product
     * @param productDetailedDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/add")
    public ResponseEntity<String> addProduct(@RequestBody ProductDetailedDTO productDetailedDTO) {
        LOGGER.info("Request for creating a new product");
        return this.productServiceImpl.addProduct(productDetailedDTO);
    }

    /**
     * Updates an existing product by id
     * @param id
     * @param productDetailedDTO
     * @return ResponseEntity<String>
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateProductById(@PathVariable UUID id, @RequestBody ProductDetailedDTO productDetailedDTO) {
        LOGGER.info("Request for updating of a product by id");
        return this.productServiceImpl.updateProductById(id, productDetailedDTO);
    }

    /**
     * Deletes a product by id
     * @param id
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable UUID id) {
        LOGGER.info("Request for deleting an user by id");
        return this.productServiceImpl.deleteProductById(id);
    }
}
