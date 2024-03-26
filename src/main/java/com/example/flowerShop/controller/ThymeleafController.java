package com.example.flowerShop.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@CrossOrigin("*")
public class ThymeleafController {

    @GetMapping("/")
    public ModelAndView hello() {
      return new ModelAndView("home");
    }
}