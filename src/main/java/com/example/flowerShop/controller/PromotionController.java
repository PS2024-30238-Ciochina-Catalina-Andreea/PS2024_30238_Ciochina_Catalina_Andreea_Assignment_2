package com.example.flowerShop.controller;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.promotion.PromotionDTO;
import com.example.flowerShop.dto.promotion.PromotionDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.service.impl.PromotionServiceImpl;
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
@RequestMapping("/promotion")
@CrossOrigin("*")
public class PromotionController {

    private final PromotionServiceImpl promotionService;

    @Autowired
    private HttpSession session;

    @Autowired
    public PromotionController(PromotionServiceImpl promotionService) {
        this.promotionService = promotionService;
    }

    @GetMapping("/get/all")
    public ModelAndView getAllPromotions() {
        ModelAndView modelAndView = new ModelAndView("listOfPromotions");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        List<PromotionDTO> promotions = this.promotionService.getAllPromotions().getBody();
        modelAndView.addObject("promotions", promotions);
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<PromotionDTO> getPromotionById(@PathVariable UUID id) {
        return this.promotionService.getPromotionById(id);
    }

    @GetMapping("/createPromotion")
    public ModelAndView getAllProductsForPromotion() {
        ModelAndView modelAndView = new ModelAndView("createPromotion");
        List<ProductDetailedDTO> prods = this.promotionService.getAllProductsForPromotion().getBody();
        modelAndView.addObject("products", prods);
        return modelAndView;
    }


    @PostMapping("/add")
    public ModelAndView addPromotion(@RequestBody PromotionDetailedDTO promotionDetailedDTO) {
        this.promotionService.addPromotion(promotionDetailedDTO);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("/promotion/get/all"));
        return modelAndView;
    }

    @GetMapping("/updatePromotion/{id}")
    public ModelAndView updatePromotion(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("updatePromotion");
        modelAndView.addObject("promotion", this.promotionService.getPromotionById(id).getBody());
        return modelAndView;
    }

    @PostMapping("/update/{id}")
    public RedirectView updatePromotionById(@PathVariable UUID id, @ModelAttribute("promotion") PromotionDetailedDTO promotionDetailedDTO, HttpServletRequest request) {
        ResponseEntity<String> response = this.promotionService.updatePromotionById(id, promotionDetailedDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new RedirectView("/promotion/get/all");
        }else{
            String referer = request.getHeader("Referer");
            return new RedirectView(referer);
        }
    }

    @PostMapping("/delete/{id}")
    public RedirectView deletePromotionById(@PathVariable UUID id) {
        this.promotionService.deletePromotionById(id);
        return new RedirectView("/promotion/get/all");
    }
}
