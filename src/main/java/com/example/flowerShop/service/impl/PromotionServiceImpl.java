package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.PromotionConstants;
import com.example.flowerShop.dto.promotion.PromotionDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.entity.*;
import com.example.flowerShop.mapper.PromotionMapper;
import com.example.flowerShop.repository.ProductRepository;
import com.example.flowerShop.repository.PromotionRepository;
import com.example.flowerShop.service.PromotionService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.promotion.PromotionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PromotionServiceImpl implements PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final ProductRepository productRepository;
    private final PromotionUtils promotionUtils;

    @Autowired
    public PromotionServiceImpl(PromotionRepository promotionRepository,
                                PromotionMapper promotionMapper,
                                ProductRepository productRepository,
                                PromotionUtils promotionUtils){
        this.promotionMapper = promotionMapper;
        this.promotionRepository = promotionRepository;
        this.productRepository = productRepository;
        this.promotionUtils = promotionUtils;
    }

    @Override
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {

        try {
            List<Promotion> promotions = promotionRepository.findAll();
            return new ResponseEntity<>(promotionMapper.convertListToDtoWithObjects(promotions), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<PromotionDTO> getPromotionById(UUID id) {
        try {
            Optional<Promotion> promotion = promotionRepository.findById(id);
            if (promotion.isPresent()) {
                Promotion promotionExisting = promotion.get();
                return new ResponseEntity<>(promotionMapper.convertEntToDtoWithObjects(promotionExisting), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addPromotion(PromotionDetailedDTO promotionDetailedDTO) {
        try {
            if (this.promotionUtils.validatePromotionMap(promotionDetailedDTO)) {

                List<Product> products = productRepository.findProjectedByIdIn(promotionDetailedDTO.getId_products());

                if (products.stream().allMatch(Objects::nonNull) && !products.isEmpty()) {
                    PromotionDTO promotionDTO = promotionMapper.convToDtoWithObjects(promotionDetailedDTO, products);
                    promotionRepository.save(promotionMapper.convertToEntity(promotionDTO));
                    return Utils.getResponseEntity(PromotionConstants.PROMOTION_CREATED, HttpStatus.CREATED);
                } else {
                    return Utils.getResponseEntity(PromotionConstants.SOMETHING_WENT_WRONG_AT_CREATING_PROMOTION, HttpStatus.BAD_REQUEST);
                }
            } else {
                return Utils.getResponseEntity(PromotionConstants.INVALID_DATA_AT_CREATING_PROMOTION, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(PromotionConstants.SOMETHING_WENT_WRONG_AT_CREATING_PROMOTION, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> updatePromotionById(UUID id, PromotionDetailedDTO promotionDetailedDTO) {
        return null;
    }

    @Override
    public ResponseEntity<String> deletePromotionById(UUID id) {
        return null;
    }
}
