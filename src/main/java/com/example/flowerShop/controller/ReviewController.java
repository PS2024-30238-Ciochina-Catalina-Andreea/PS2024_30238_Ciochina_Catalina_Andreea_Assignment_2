package com.example.flowerShop.controller;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.review.ReviewDTO;
import com.example.flowerShop.dto.review.ReviewDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.service.impl.ReviewServiceImpl;
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

import java.util.*;

@RestController
@RequestMapping("/review")
@CrossOrigin("*")
public class ReviewController {

    private final ReviewServiceImpl reviewService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param reviewService
     */
    @Autowired
    public ReviewController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * Request for rewies list by product id
     *
     * @param id
     * @return ResponseEntity<List < ReviewDTO>>
     */
    @GetMapping("/getAllByProduct/{id}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsByProductId(@PathVariable UUID id) {
        LOGGER.info("Request for list of reviews by product");
        return this.reviewService.getAllReviewsByProductId(id);
    }

    /**
     * Retrieves a list with all reviews and displays it on a page
     *
     * @return ModelAndView
     */
    @GetMapping("/listOfReviews")
    public ModelAndView getAllProducts() {
        LOGGER.info("Retrieve list of reviews");
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

    /**
     * Gets review by id
     *
     * @param id
     * @return ResponseEntity<ReviewDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable UUID id) {
        LOGGER.info("Revtrieves review with id {}", id);
        return this.reviewService.getReviewById(id);
    }

    /**
     * Create a new review on a chosen product, redirects the user to the create review page
     *
     * @return ModelAndView
     */
    @GetMapping("/createReview")
    public ModelAndView createReview() {
        ModelAndView modelAndView = new ModelAndView("createReview");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        List<ProductDetailedDTO> prods = this.reviewService.getAllProductsForReview().getBody();
        modelAndView.addObject("products", prods);
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    /**
     * The current user logged in creates a new review on a chosen product
     *
     * @param detailedDTO
     * @param request
     * @return ModelAndView
     */
    @PostMapping("/add")
    public ModelAndView addReview(@RequestBody ReviewDetailedDTO detailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.reviewService.addReview(detailedDTO);
        ModelAndView modelAndView = new ModelAndView();
        if (response.getStatusCode() == HttpStatus.CREATED) {
            LOGGER.info("Review was created");
            modelAndView.setView(new RedirectView("/review/listOfReviews"));
        } else {
            LOGGER.info("Review was not created");
            String referer = request.getHeader("Referer");
            modelAndView.setView(new RedirectView(referer));
        }
        return modelAndView;

    }

    /**
     * Update Review page
     *
     * @param id
     * @return ModelAndView
     */
    @GetMapping("/updateReview/{id}")
    public ModelAndView updateReview(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("updateReview");
        modelAndView.addObject("review", this.reviewService.getReviewById(id).getBody());
        return modelAndView;
    }

    /**
     * Updates the review that the current user made, in success redirects to list of reviews else back on update page
     *
     * @param id
     * @param reviewDetailedDTO
     * @param request
     * @return RedirectView
     */
    @PostMapping("/update/{id}")
    public RedirectView updateReviewById(@PathVariable UUID id, @ModelAttribute("review") ReviewDetailedDTO reviewDetailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.reviewService.updateReviewById(id, reviewDetailedDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("Review was successfully updated");
            return new RedirectView("/review/listOfReviews");
        } else {
            LOGGER.info("Review was not updated");
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }

    /**
     * Request for deleting of a review, just the current user can delete its own reviews
     *
     * @param id
     * @param request
     * @return RedirectView
     */
    @PostMapping("/delete/{id}")
    public RedirectView deleteReviewById(@PathVariable UUID id, HttpServletRequest request) {
        ResponseEntity<String> response = this.reviewService.deleteReviewById(id);
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.info("Review was successfully deleted");
            return new RedirectView("/review/listOfReviews");
        } else {
            LOGGER.info("Review was not deleted");
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }
}
