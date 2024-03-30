package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.ProductConstants;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.mapper.ProductMapper;
import com.example.flowerShop.dto.product.ProductDTO;
import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.entity.Category;
import com.example.flowerShop.entity.Product;

import com.example.flowerShop.repository.CategoryRepository;
import com.example.flowerShop.repository.ProductRepository;
import com.example.flowerShop.service.ProductService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.category.CategoryName;
import com.example.flowerShop.utils.product.ProductUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    private final CategoryRepository categoryRepository;

    private final ProductUtils productUtils;

    private final ProductMapper productMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);

    /**
     * Injected constructor
     *
     * @param productRepository
     * @param categoryRepository
     * @param productUtils
     * @param productMapper
     */
    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, CategoryRepository categoryRepository, ProductUtils productUtils, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.productUtils = productUtils;
        this.productMapper = productMapper;
    }

    /**
     * Retrieves a list of products entries from the db
     *
     * @return ResponseEntity<List < ProductDetailedDTO>>
     */
    @Override
    public ResponseEntity<List<ProductDetailedDTO>> getAllProducts() {

        LOGGER.info("Fetching products list...");
        try {
            List<Product> products = productRepository.findAll();
            LOGGER.info("Fetching completed, list of products retrieved");
            return new ResponseEntity<>(productMapper.convertListToDTO(products), HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Error while performing the fetching of the list with products", exception);
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets a product given by id
     *
     * @param id
     * @return ResponseEntity<ProductDetailedDTO>
     */
    @Override
    public ResponseEntity<ProductDetailedDTO> getProductById(UUID id) {

        LOGGER.info("Fetching product with id = " + id);
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                Product productExisting = productOptional.get();
                LOGGER.info("Fetching completed, product retrieved");
                return new ResponseEntity<>(productMapper.convertToDTO(productExisting), HttpStatus.OK);
            } else {
                LOGGER.error("Product with id = {} not found in the db", id);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Error while retrieving the product by id");
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a new product entry in the db
     *
     * @param productDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addProduct(ProductDetailedDTO productDetailedDTO) {

        LOGGER.info("Creating a new product...");
        try {
            if (this.productUtils.validateProductMap(productDetailedDTO)) {
                Optional<Product> productOptional = productRepository.findByName(productDetailedDTO.getName());
                if (productOptional.isEmpty()) {
                    Optional<Category> category = categoryRepository.findByName(CategoryName.valueOf(productDetailedDTO.getCategory()));
                    if (category.isPresent()) {
                        ProductDTO productDTO = productMapper.convToProdWithCategory(productDetailedDTO, category);
                        LOGGER.info("Product created");
                        productRepository.save(productMapper.convertToEntity(productDTO));
                        return Utils.getResponseEntity(ProductConstants.PRODUCT_CREATED, HttpStatus.CREATED);
                    } else {
                        LOGGER.error("Category with this name does not exist");
                        return Utils.getResponseEntity(ProductConstants.INVALID_DATA_AT_CREATING_PRODUCT, HttpStatus.BAD_REQUEST);
                    }
                } else {
                    LOGGER.error("Product with this name already exists");
                    return Utils.getResponseEntity(ProductConstants.PRODUCT_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                LOGGER.error("Invalid data was sent for creating the product");
                return Utils.getResponseEntity(ProductConstants.INVALID_DATA_AT_CREATING_PRODUCT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at creating the product");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG_AT_CREATING_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates an existing product from the db given by an id
     *
     * @param id
     * @param productDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updateProductById(UUID id, ProductDetailedDTO productDetailedDTO) {

        LOGGER.info("Updating the data for a product with id {}...", id);
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            Optional<Category> category;
            if (productOptional.isPresent()) {
                Product productExisting = productOptional.get();
                if (Objects.nonNull(productDetailedDTO.getCategory())) {
                    category = categoryRepository.findByName(CategoryName.valueOf(productDetailedDTO.getCategory()));
                }
                else
                    category = categoryRepository.findByName(productExisting.getCategory().getName());
                ProductDTO productDTO = productMapper.convToProdWithCategory(productDetailedDTO, category);
                ProductUtils.updateProductValues(productExisting, productDTO);
                LOGGER.info("Completed product update");
                productRepository.save(productExisting);
                return Utils.getResponseEntity(ProductConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error("Product with id = {} not found in the db", id);
                return Utils.getResponseEntity(ProductConstants.INVALID_PRODUCT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at updating the product");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG_AT_UPDATING_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes an existing product given by an id
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteProductById(UUID id) {

        LOGGER.info("Deleting the product with id {}...", id);
        try {
            Optional<Product> productOptional = productRepository.findById(id);
            if (productOptional.isPresent()) {
                productRepository.deleteById(id);
                LOGGER.info("Product deleted successfully");
                return Utils.getResponseEntity(ProductConstants.PRODUCT_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("Product with id = {} not found in the db", id);
                return Utils.getResponseEntity(ProductConstants.INVALID_PRODUCT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Something went wrong at deleting the product");
            e.printStackTrace();
        }
        return Utils.getResponseEntity(ProductConstants.SOMETHING_WENT_WRONG_AT_DELETING_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public Product convertToModelObject(UUID id) {
        ProductDetailedDTO productDTO = this.getProductById(id).getBody();
        Optional<Category> categoryOptional = categoryRepository.findByName(CategoryName.valueOf(productDTO.getCategory()));
        Product product = new Product();
        if (productDTO != null) {
            product.setId(productDTO.getId());
            product.setName(productDTO.getName());
            product.setDescription(productDTO.getDescription());
            product.setStock(productDTO.getStock());
            product.setPrice(productDTO.getPrice());
            product.setCategory(categoryOptional.get());
        }
        return product;
    }
}
