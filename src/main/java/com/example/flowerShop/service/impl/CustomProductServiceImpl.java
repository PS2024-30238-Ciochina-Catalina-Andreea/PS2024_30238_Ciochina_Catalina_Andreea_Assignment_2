package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.CustomProductConstants;
import com.example.flowerShop.dto.customProduct.CustomProductDTO;
import com.example.flowerShop.dto.customProduct.CustomProductDetailedDTO;;
import com.example.flowerShop.entity.CustomProduct;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.mapper.CustomProductMapper;
import com.example.flowerShop.repository.CustomProductRepository;
import com.example.flowerShop.repository.ProductRepository;
import com.example.flowerShop.repository.UserRepository;
import com.example.flowerShop.service.CustomProductService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.customProduct.CustomProductUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomProductServiceImpl implements CustomProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CustomProductMapper customProductMapper;
    private final CustomProductRepository customProductRepository;
    private final CustomProductUtils customProductUtils;

    @Autowired
    public CustomProductServiceImpl(ProductRepository productRepository,
                                    CustomProductMapper customProductMapper,
                                    CustomProductRepository customProductRepository,
                                    CustomProductUtils customProductUtils,
                                    UserRepository userRepository) {
        this.customProductMapper = customProductMapper;
        this.customProductRepository = customProductRepository;
        this.productRepository = productRepository;
        this.customProductUtils = customProductUtils;
        this.userRepository = userRepository;

    }

    @Override
    public ResponseEntity<List<CustomProductDTO>> getAllCustomProducts() {
        try {
            List<CustomProduct> customProducts = customProductRepository.findAll();
            return new ResponseEntity<>(customProductMapper.convertListToDtoWithObjects(customProducts), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<CustomProductDTO> getCustomProductById(UUID id) {
        try {
            Optional<CustomProduct> customProduct = customProductRepository.findById(id);
            if (customProduct.isPresent()) {
                CustomProduct customProductEx = customProduct.get();
                return new ResponseEntity<>(customProductMapper.convertEntToDtoWithObjects(customProductEx), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addCustomProduct(CustomProductDetailedDTO customProductDetailedDTO) {
        try {
            if (this.customProductUtils.validateCustomProductMap(customProductDetailedDTO)) {

                Optional<User> user = userRepository.findById(customProductDetailedDTO.getId_user());
                List<Product> products = productRepository.findProjectedByIdIn(customProductDetailedDTO.getId_products());

                if (products.stream().allMatch(Objects::nonNull) && !products.isEmpty()) {
                    CustomProductDTO customProductDTO = customProductMapper.convToDtoWithObjects(customProductDetailedDTO, user, products);
                    customProductRepository.save(customProductMapper.convertToEntity(customProductDTO));
                    return Utils.getResponseEntity(CustomProductConstants.CUSTOM_PRODUCT_CREATED, HttpStatus.CREATED);
                } else {
                    return Utils.getResponseEntity(CustomProductConstants.SOMETHING_WENT_WRONG_AT_CREATING_CUSTOM_PRODUCT, HttpStatus.BAD_REQUEST);
                }
            } else {
                return Utils.getResponseEntity(CustomProductConstants.INVALID_DATA_AT_CREATING_CUSTOM_PRODUCT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(CustomProductConstants.SOMETHING_WENT_WRONG_AT_CREATING_CUSTOM_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteCustomProductById(UUID id) {
        try {
            Optional<CustomProduct> customProduct = customProductRepository.findById(id);
            if (customProduct.isPresent()) {
                customProductRepository.deleteById(id);
                return Utils.getResponseEntity(CustomProductConstants.CUSTOM_PRODUCT_DELETED, HttpStatus.OK);
            } else {
                return Utils.getResponseEntity(CustomProductConstants.INVALID_CUSTOM_PRODUCT, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(CustomProductConstants.SOMETHING_WENT_WRONG_AT_DELETING_CUSTOM_PRODUCT, HttpStatus.INTERNAL_SERVER_ERROR);

    }
}
