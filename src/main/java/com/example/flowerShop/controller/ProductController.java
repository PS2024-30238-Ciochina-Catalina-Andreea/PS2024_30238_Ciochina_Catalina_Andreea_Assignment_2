package com.example.flowerShop.controller;

import com.example.flowerShop.dto.product.ProductDetailedDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.entity.Product;
import com.example.flowerShop.service.impl.ProductServiceImpl;
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
@RequestMapping("/product")
@CrossOrigin("*")
public class ProductController {

    private final ProductServiceImpl productServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param productServiceImpl
     */
    @Autowired
    public ProductController(ProductServiceImpl productServiceImpl) {
        this.productServiceImpl = productServiceImpl;
    }

    /**
     * Gets list of products from db
     *
     * @return ModelAndView
     */
    @GetMapping("/listOfProducts")
    public ModelAndView getAllProducts() {
        LOGGER.info("Request for list of products");
        ModelAndView modelAndView = new ModelAndView("listOfProducts");
        List<ProductDetailedDTO> products = this.productServiceImpl.getAllProducts().getBody();
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        modelAndView.addObject("products", products);
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    /**
     * Gets product by id
     *
     * @param id
     * @return ResponseEntity<ProductDetailedDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<ProductDetailedDTO> getProductById(@PathVariable UUID id) {
        LOGGER.info("Request for product with id={}", id);
        return this.productServiceImpl.getProductById(id);
    }

    /**
     * Create product view
     *
     * @return ModelAndView
     */
    @GetMapping("/add")
    public ModelAndView createProduct() {
        ModelAndView modelAndView = new ModelAndView("createProduct");
        modelAndView.addObject("product", new Product());
        return modelAndView;
    }

    /**
     * Creates a new product
     *
     * @param productDetailedDTO
     * @return ResponseEntity<String>
     */
    @PostMapping("/createProduct")
    public RedirectView addProduct(@ModelAttribute("product") ProductDetailedDTO productDetailedDTO) {
        LOGGER.info("Request for creating a new product");
        ResponseEntity<String> stringResponseEntity = this.productServiceImpl.addProduct(productDetailedDTO);
        if (stringResponseEntity.getStatusCode() == HttpStatus.CREATED) {
            return new RedirectView("/product/listOfProducts");
        }
        return new RedirectView("/product/add");
    }

    /**
     * Update product view
     *
     * @param id
     * @return ModelAndView
     */
    @GetMapping("/updateProduct/{id}")
    public ModelAndView updateUser(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("updateProduct");
        modelAndView.addObject("product", this.getProductById(id).getBody());
        return modelAndView;
    }

    /**
     * Updates an existing product by id
     *
     * @param id
     * @param productDetailedDTO
     * @return RedirectView
     */
    @PostMapping("/update/{id}")
    public RedirectView updateProductById(@PathVariable UUID id, @ModelAttribute("product") ProductDetailedDTO productDetailedDTO) {
        LOGGER.info("Request for updating of a product by id");
        ResponseEntity<String> response = this.productServiceImpl.updateProductById(id, productDetailedDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            return new RedirectView("/product/listOfProducts");
        }
        return new RedirectView("/product/updateProduct/" + id);
    }

    /**
     * Deletes a product by id
     *
     * @param id
     * @return RedirectView
     */
    @PostMapping("/delete/{id}")
    public RedirectView deleteProductById(@PathVariable UUID id, HttpServletRequest request) {
        LOGGER.info("Request for deleting an user by id");
        this.productServiceImpl.deleteProductById(id);
        return new RedirectView("/product/listOfProducts?");
    }
}
