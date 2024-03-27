package com.example.flowerShop.controller;

import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.service.impl.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * Injected constructor
     * @param userServiceImpl
     */
    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * Gets list of users
     * @return
     */
    @GetMapping("/get/all")
    public ModelAndView getAllUsers() {
        ModelAndView modelAndView = new ModelAndView("listOfUsers");
        List<UserGetDTO> users = this.userServiceImpl.getAllUsers().getBody();
        modelAndView.addObject("users", users);
        LOGGER.info("Request for list of users");
        return modelAndView;
    }

    /**
     * Gets user by id
     * @param id
     * @return ResponseEntity<UserGetDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable UUID id) {
        LOGGER.info("Request for a user by id");
        return this.userServiceImpl.getUserById(id);
    }

    /**
     * Creates a new user
     * @param user
     * @return ResponseEntity<String>
     */
    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestBody UserPostDTO user) {
        LOGGER.info("Request for creating a new user");
        return this.userServiceImpl.addUser(user);
    }

    /**
     * Updates user by given id and request body
     * @param id
     * @param user
     * @return ResponseEntity<String>
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUserById(@PathVariable UUID id, @RequestBody UserPostDTO user) {
        LOGGER.info("Request for updating data for a user by id");
        return this.userServiceImpl.updateUserById(id, user);
    }

    /**
     * Deletes user by given id
     * @param id
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id) {
        LOGGER.info("Request for deleting a user by id");
        return this.userServiceImpl.deleteUserById(id);
    }

}
