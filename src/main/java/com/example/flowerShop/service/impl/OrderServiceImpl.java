package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.OrderConstants;
import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.dto.order.OrderDetailedDTO;
import com.example.flowerShop.entity.Order;
import com.example.flowerShop.entity.OrderItem;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.mapper.OrderMapper;
import com.example.flowerShop.repository.OrderItemRepository;
import com.example.flowerShop.repository.OrderRepository;
import com.example.flowerShop.repository.UserRepository;
import com.example.flowerShop.service.OrderService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.order.OrderUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final UserRepository userRepository;

    private final OrderUtils orderUtils;

    private final OrderMapper orderMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderItemServiceImpl.class);

    /**
     * Injected cosntructor
     * @param orderRepository
     * @param orderItemRepository
     * @param userRepository
     * @param orderUtils
     * @param orderMapper
     */
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, UserRepository userRepository, OrderUtils orderUtils, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
        this.orderUtils = orderUtils;
        this.orderMapper = orderMapper;
    }

    /**
     * Gets a list of the orders from the db
     * @return ResponseEntity<List<OrderDTO>>
     */
    @Override
    public ResponseEntity<List<OrderDTO>> getAllOrders() {

        LOGGER.info("Fetching orders list...");
        try {
            List<Order> orders = orderRepository.findAll();
            LOGGER.info("Fetching completed, list of orders retrieved");
            return new ResponseEntity<>(orderMapper.convertListToDtoWithObjects(orders), HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Error while performing the fetching of the list with orders", exception);
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets an order by a given id
     * @param id
     * @return ResponseEntity<OrderDTO>
     */
    @Override
    public ResponseEntity<OrderDTO> getOrderById(UUID id) {

        LOGGER.info("Fetching order with id = " + id);
        try {
            Optional<Order> order = orderRepository.findById(id);
            if (order.isPresent()) {
                Order orderExisting = order.get();
                LOGGER.info(String.valueOf(orderExisting.getOrderItems().size()));
                LOGGER.info("Fetching completed, order retrieved");
                return new ResponseEntity<>(orderMapper.convertEntToDtoWithObjects(orderExisting), HttpStatus.OK);
            } else {
                LOGGER.error("Order with id = {} not found in the db", id);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Error while retrieving the order by id");
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a new order entry in the db
     * @param orderDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addOrder(OrderDetailedDTO orderDetailedDTO) {

        LOGGER.info("Creating a new order...");
        try {
            if (this.orderUtils.validateOrderMap(orderDetailedDTO)) {

                Optional<User> user = userRepository.findById(orderDetailedDTO.getId_user());
                List<OrderItem> items = orderItemRepository.findProjectedByIdIn(orderDetailedDTO.getId_orderItems());

                if (user.isPresent() && items.stream().allMatch(Objects::nonNull) && !items.isEmpty()) {
                    OrderDTO orderDTO = orderMapper.convToDtoWithObjects(orderDetailedDTO, items, user);
                    LOGGER.info("Order created");
                    orderRepository.save(orderMapper.convertToEntity(orderDTO));
                    return Utils.getResponseEntity(OrderConstants.ORDER_CREATED, HttpStatus.CREATED);
                } else {
                    LOGGER.error("Invalid data was sent for creating the order");
                    return Utils.getResponseEntity(OrderConstants.INVALID_DATA_AT_CREATING_ORDER, HttpStatus.BAD_REQUEST);
                }
            } else {
                LOGGER.error("Invalid data was sent for creating the order");
                return Utils.getResponseEntity(OrderConstants.INVALID_DATA_AT_CREATING_ORDER, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at creating the order");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(OrderConstants.SOMETHING_WENT_WRONG_AT_CREATING_ORDER, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates an existing order by a given id
     * @param id
     * @param orderDetailedDTO
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updateOrderById(UUID id, OrderDetailedDTO orderDetailedDTO) {

        LOGGER.info("Updating the data for an order with id {}...", id);
        try {
            Optional<Order> orderOptional = orderRepository.findById(id);
            if (orderOptional.isPresent()) {
                Order orderExisting = orderOptional.get();
                if (orderDetailedDTO.getId_user() != null || orderDetailedDTO.getId_orderItems() != null) {
                    LOGGER.error("You cannot modify user or list of shopped items");
                }
                OrderUtils.updateOrderValues(orderExisting, orderDetailedDTO);
                LOGGER.info("Completed order update");
                orderRepository.save(orderExisting);
                return Utils.getResponseEntity(OrderConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error("Order with id = {} not found in the db", id);
                return Utils.getResponseEntity(OrderConstants.INVALID_ORDER, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at updating the order");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(OrderConstants.SOMETHING_WENT_WRONG_AT_UPDATING_ORDER, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes an existing order given by an id
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteOrderById(UUID id) {

        LOGGER.info("Deleting the order with id {}...", id);
        try {
            Optional<Order> orderOptional = orderRepository.findById(id);
            if (orderOptional.isPresent()) {
                orderRepository.deleteById(id);
                LOGGER.info("Order deleted successfully");
                return Utils.getResponseEntity(OrderConstants.ORDER_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("Order with id = {} not found in the db", id);
                return Utils.getResponseEntity(OrderConstants.INVALID_ORDER, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Something went wrong at deleting the order");
            e.printStackTrace();
        }
        return Utils.getResponseEntity(OrderConstants.SOMETHING_WENT_WRONG_AT_DELETING_ORDER, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
