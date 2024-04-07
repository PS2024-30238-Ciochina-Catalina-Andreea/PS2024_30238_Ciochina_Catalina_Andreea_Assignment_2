package com.example.flowerShop.controller;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.review.ReviewDTO;
import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.entity.Review;
import com.example.flowerShop.service.impl.ReviewServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.*;

@RestController
@RequestMapping("/review")
@CrossOrigin("*")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    @Autowired
    private HttpSession session;

    @Autowired
    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/getAllByProduct/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByProductId(@PathVariable UUID id) {
        return this.reviewService.getAllReviewsByProductId(id);
    }

    @GetMapping("/listOfReviews")
    public ModelAndView getAllProducts() {
        ModelAndView modelAndView = new ModelAndView("listOfReviews");
        List<ReviewDTO> reviewDTOS = this.reviewService.getAllReviews().getBody();
        Map<String, List<ReviewDTO>> reviewsByProduct = new HashMap<>();
        assert reviewDTOS != null;
        for (ReviewDTO review : reviewDTOS) {
            String productName = review.getProduct().getName();
            reviewsByProduct.computeIfAbsent(productName, k -> new ArrayList<>()).add(review);
        }
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        modelAndView.addObject("reviewsByProduct", reviewsByProduct);
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable UUID id) {
        return this.reviewService.getReviewById(id);
    }

    @GetMapping("/createReview")
    public ModelAndView getAllProductsForPromotion() {
        ModelAndView modelAndView = new ModelAndView("createReview");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        List<ProductDetailedDTO> prods = this.reviewService.getAllProductsForReview().getBody();
        modelAndView.addObject("products", prods);
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addReview(@RequestBody ReviewDetailedDTO detailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.reviewService.addReview(detailedDTO);
        ModelAndView modelAndView = new ModelAndView();
        if (response.getStatusCode() == HttpStatus.CREATED) {
            modelAndView.setView(new RedirectView("/review/listOfReviews"));
        } else {
            String referer = request.getHeader("Referer");
            modelAndView.setView(new RedirectView(referer));
        }
        return modelAndView;

    }

    @GetMapping("/updateReview/{id}")
    public ModelAndView updatePromotion(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("updateReview");
        modelAndView.addObject("review", this.reviewService.getReviewById(id).getBody());
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public RedirectView updateReviewById(@PathVariable UUID id, @ModelAttribute("review") ReviewDetailedDTO reviewDetailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.reviewService.updateReviewById(id, reviewDetailedDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new RedirectView("/review/listOfReviews");
        } else {
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }

    @PostMapping("/delete/{id}")
    public RedirectView deleteReviewById(@PathVariable UUID id, HttpServletRequest request) {
        ResponseEntity<String> response = this.reviewService.deleteReviewById(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new RedirectView("/review/listOfReviews");
        } else {
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }
}
