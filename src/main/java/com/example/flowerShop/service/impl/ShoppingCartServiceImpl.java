package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.ShoppingCartConstants;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDTO;
import com.example.flowerShop.dto.shoppingCart.ShoppingCartDetailedDTO;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.ShoppingCart;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.mapper.ShoppingCartMapper;
import com.example.flowerShop.repository.OrderItemRepository;
import com.example.flowerShop.repository.OrderRepository;
import com.example.flowerShop.repository.ShoppingCartRepository;
import com.example.flowerShop.repository.UserRepository;
import com.example.flowerShop.service.ShoppingCartService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.shoppingCart.ShoppingCartUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final ShoppingCartMapper shoppingCartMapper;
    private final ShoppingCartUtils shoppingCartUtils;
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserRepository userRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    @Autowired
    public ShoppingCartServiceImpl(ShoppingCartMapper shoppingCartMapper,
                                   ShoppingCartRepository shoppingCartRepository,
                                   ShoppingCartUtils shoppingCartUtils,
                                   UserRepository userRepository,
                                   OrderItemRepository orderItemRepository,
                                   OrderRepository orderRepository) {
        this.shoppingCartMapper = shoppingCartMapper;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartUtils = shoppingCartUtils;
        this.userRepository = userRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
    }

    @Override
    public ResponseEntity<List<ShoppingCartDTO>> getAllCarts() {
        try {
            List<ShoppingCart> shoppingCarts = shoppingCartRepository.findAll();
            return new ResponseEntity<>(shoppingCartMapper.convertListToDtoWithObjects(shoppingCarts), HttpStatus.OK);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<ShoppingCartDTO> getCartById(UUID id) {
        try {
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
            if (shoppingCart.isPresent()) {
                ShoppingCart cartExisting = shoppingCart.get();
                return new ResponseEntity<>(shoppingCartMapper.convertEntToDtoWithObjects(cartExisting), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> addCart(ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        try {
            if (this.shoppingCartUtils.validateCartMap(shoppingCartDetailedDTO)) {

                Optional<User> user = userRepository.findById(shoppingCartDetailedDTO.getId_user());
                List<OrderItem> items = orderItemRepository.findProjectedByIdIn(shoppingCartDetailedDTO.getId_orderItems());

                if (user.isPresent() && items.stream().allMatch(Objects::nonNull) && !items.isEmpty()) {
                    ShoppingCartDTO shoppingCartDTO = shoppingCartMapper.convToDtoWithObjects(shoppingCartDetailedDTO, items, user);
                    shoppingCartRepository.save(shoppingCartMapper.convertToEntity(shoppingCartDTO));
                    return Utils.getResponseEntity(ShoppingCartConstants.CART_CREATED, HttpStatus.CREATED);
                } else {
                    return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_CREATING_CART, HttpStatus.BAD_REQUEST);
                }
            } else {
                return Utils.getResponseEntity(ShoppingCartConstants.INVALID_DATA_AT_CREATING_CART, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_CREATING_CART, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCartById(UUID id, ShoppingCartDetailedDTO shoppingCartDetailedDTO) {
        try {
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
            List<OrderItem> items = orderItemRepository.findProjectedByIdIn(shoppingCartDetailedDTO.getId_orderItems());
            if (shoppingCart.isPresent()) {
                ShoppingCart existingCart = shoppingCart.get();
                if (shoppingCartDetailedDTO.getId_user() != null || shoppingCartDetailedDTO.getId_order() != null) {

                }
                ShoppingCartUtils.updateCartValues(existingCart, shoppingCartDetailedDTO, items);
                shoppingCartRepository.save(existingCart);
                return Utils.getResponseEntity(ShoppingCartConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                return Utils.getResponseEntity(ShoppingCartConstants.INVALID_CART, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_UPDATING_CART, HttpStatus.INTERNAL_SERVER_ERROR);

    }

    @Override
    public ResponseEntity<String> deleteCartById(UUID id) {
        try {
            Optional<ShoppingCart> shoppingCart = shoppingCartRepository.findById(id);
            if (shoppingCart.isPresent()) {
                shoppingCartRepository.deleteById(id);
                return Utils.getResponseEntity(ShoppingCartConstants.CART_DELETED, HttpStatus.OK);
            } else {
                return Utils.getResponseEntity(ShoppingCartConstants.INVALID_CART, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Utils.getResponseEntity(ShoppingCartConstants.SOMETHING_WENT_WRONG_AT_DELETING_CART, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
