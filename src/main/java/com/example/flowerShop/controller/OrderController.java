package com.example.flowerShop.controller;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.dto.order.OrderDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.entity.Order;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.service.impl.OrderServiceImpl;
import com.example.flowerShop.utils.user.UserRole;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/order")
@CrossOrigin("*")
public class OrderController {

    private final OrderServiceImpl orderServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private HttpSession session;

    /**
     * Constructor with injection
     *
     * @param orderServiceImpl
     */
    @Autowired
    public OrderController(OrderServiceImpl orderServiceImpl) {
        this.orderServiceImpl = orderServiceImpl;
    }

    /**
     * Gets list of orders
     *
     * @return ResponseEntity<List < OrderDTO>>
     */
    @GetMapping("/get/all")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        LOGGER.info("Request for list of orders");
        return this.orderServiceImpl.getAllOrders();
    }

    @GetMapping("/getByUser/all/{id}")
    public ModelAndView getAllOrdersByUser(@PathVariable UUID id) {
        LOGGER.info("Request for list of orders by user");
        ModelAndView modelAndView = new ModelAndView("listOfOrders");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        List<OrderDTO> orders = null;
        if (currentUser.getRole().equals(UserRole.ADMIN))
            orders = this.orderServiceImpl.getAllOrders().getBody();
        else
            orders = this.orderServiceImpl.getAllOrdersByUser(id).getBody();

        modelAndView.addObject("user", currentUser);
        modelAndView.addObject("orders", orders);
        return modelAndView;
    }

    /**
     * Retrieves order by given id
     *
     * @param id
     * @return ResponseEntity<OrderDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable UUID id) {
        LOGGER.info("Request for order data by id");
        return this.orderServiceImpl.getOrderById(id);
    }

    /**
     * Creates a new order
     *
     * @param orderDetailedDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/add")
    public RedirectView addOrder(@RequestBody OrderDetailedDTO orderDetailedDTO) {
        LOGGER.info("Request for creating a new order");
        this.orderServiceImpl.addOrder(orderDetailedDTO);
        return new RedirectView("/order/getByUser/all/" + orderDetailedDTO.getId_user());
    }

    @GetMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("updateOrder");
        modelAndView.addObject("order", this.getOrderById(id).getBody());
        return modelAndView;
    }

    /**
     * Updates an existing order searched by id
     *
     * @param id
     * @param orderDetailedDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/update/{id}")
    public RedirectView updateOrderById(@PathVariable UUID id, @ModelAttribute("order") OrderDetailedDTO orderDetailedDTO) {
        LOGGER.info("Request for updating an order by id");
        ResponseEntity<String> response = this.orderServiceImpl.updateOrderById(id, orderDetailedDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new RedirectView("/order/getByUser/all/" + id);
        }
        return new RedirectView("/order/update/" + id);
    }

    /**
     * Deletes an order by id
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @PostMapping("/delete/{id}")
    public RedirectView deleteOrderById(@PathVariable UUID id, HttpServletRequest request) {
        LOGGER.info("Request for deleting an order by id");
        this.orderServiceImpl.deleteOrderById(id);
        String referer = request.getHeader("Referer");
        return new RedirectView(referer);
    }
}
