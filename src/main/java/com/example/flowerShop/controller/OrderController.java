package com.example.flowerShop.controller;

import com.example.flowerShop.dto.order.OrderDTO;
import com.example.flowerShop.dto.order.OrderDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.OrderServiceImpl;
import com.example.flowerShop.utils.order.PaymentType;
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
     * Gets all orders placed by a user
     *
     * @param id
     * @return ModelAndView
     */
    @GetMapping("/getByUser/all/{id}")
    public ModelAndView getAllOrdersByUser(@PathVariable UUID id) {
        LOGGER.info("Request for list of orders by user");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            modelAndView = new ModelAndView("listOfOrders");
            List<OrderDTO> orders = null;
            if (currentUser.getRole().equals(UserRole.ADMIN))
                orders = this.orderServiceImpl.getAllOrders().getBody();
            else
                orders = this.orderServiceImpl.getAllOrdersByUser(id).getBody();

            modelAndView.addObject("user", currentUser);
            modelAndView.addObject("orders", orders);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
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
     * Creates a new order and if the payment type is CARD, it redirects to Pay page, else to the list of
     * placed orders by the current user
     *
     * @param orderDetailedDTO
     * @return ModelAndView
     */
    @PostMapping("/add")
    public ModelAndView addOrder(@RequestBody OrderDetailedDTO orderDetailedDTO) {
        LOGGER.info("Request for creating a new order");
        this.orderServiceImpl.addOrder(orderDetailedDTO);
        ModelAndView modelAndView = new ModelAndView();
        if (orderDetailedDTO.getPay().equals(PaymentType.CARD)) {
            modelAndView.setView(new RedirectView("/card/createPayment/" + orderDetailedDTO.getTotalPrice()));
            modelAndView.addObject("totalPrice", orderDetailedDTO.getTotalPrice());
        } else
            modelAndView.setView(new RedirectView("/order/getByUser/all/" + orderDetailedDTO.getId_user()));
        return modelAndView;
    }

    /**
     * View for update user
     *
     * @param id
     * @return ModelAndView
     */
    @GetMapping("/update/{id}")
    public ModelAndView updateUser(@PathVariable UUID id) {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView;
        if (currentUser != null) {
            if (currentUser.getRole().equals(UserRole.ADMIN)) {
                modelAndView = new ModelAndView("updateOrder");
                modelAndView.addObject("order", this.getOrderById(id).getBody());
            } else {
                modelAndView = new ModelAndView("accessDenied");
            }
            modelAndView.addObject("user", currentUser);
        } else {
            modelAndView = new ModelAndView();
            modelAndView.setView(new RedirectView("/login"));
        }
        return modelAndView;
    }

    /**
     * Updates an existing order searched by id
     *
     * @param id
     * @param orderDetailedDTO
     * @return RedirectView
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
     * @return RedirectView
     */
    @PostMapping("/delete/{id}")
    public RedirectView deleteOrderById(@PathVariable UUID id, HttpServletRequest request) {
        LOGGER.info("Request for deleting an order by id");
        ResponseEntity<String> response = this.orderServiceImpl.deleteOrderById(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
            return new RedirectView("/order/getByUser/all/" + currentUser.getId());
        } else {
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }
}
