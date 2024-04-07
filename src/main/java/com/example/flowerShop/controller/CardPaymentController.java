package com.example.flowerShop.controller;

import com.example.flowerShop.dto.cardPayment.CardPaymentDTO;
import com.example.flowerShop.dto.cardPayment.CardPaymentDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.CardPaymentServiceImpl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/card")
@CrossOrigin("*")
public class CardPaymentController {

    private final CardPaymentServiceImpl cardPaymentService;

    @Autowired
    private HttpSession session;

    @Autowired
    public CardPaymentController(CardPaymentServiceImpl cardPaymentService) {
        this.cardPaymentService = cardPaymentService;
    }

    @GetMapping("/listOfPayments")
    public ModelAndView getAllPayments() {
        ModelAndView modelAndView = new ModelAndView("listOfPayments");
        List<CardPaymentDetailedDTO> payments = this.cardPaymentService.getAllPayments().getBody();
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        modelAndView.addObject("payments", payments);
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    @GetMapping("/createPayment/{price}")
    public ModelAndView getAllPaymentsOnPage(@PathVariable(name = "price") Long price) {
        ModelAndView modelAndView = new ModelAndView("createPay");
        modelAndView.addObject("price", price);
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPayment(@ModelAttribute CardPaymentDTO cardPaymentDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.cardPaymentService.addPayment(cardPaymentDTO);
        ModelAndView modelAndView = new ModelAndView();
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        if (response.getStatusCode() == HttpStatus.CREATED) {
            modelAndView.setView(new RedirectView("/order/getByUser/all/" + currentUser.getId()));
        } else {
            String referer = request.getHeader("Referer");
            modelAndView.setView(new RedirectView(referer));
        }
        return modelAndView;
    }

    @PostMapping("/delete/{id}")
    public RedirectView deletePayment(@PathVariable UUID id) {
        this.cardPaymentService.deletePayment(id);
        return new RedirectView("/card/listOfPayments");
    }
}
