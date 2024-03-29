package com.example.flowerShop.controller;

import com.example.flowerShop.dto.user.LoginDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.service.impl.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@CrossOrigin("*")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private HttpSession session;
    /**
     * Injected constructor
     *
     * @param userServiceImpl
     */
    @Autowired
    public UserController(UserServiceImpl userServiceImpl,HttpSession session) {
        this.session = session;
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * Gets list of users
     *
     * @return ModelAndView
     */
    @GetMapping("/listOfUsers")
    public ModelAndView getAllUsers() {
        LOGGER.info("Request for list of users");
        ModelAndView modelAndView = new ModelAndView("listOfUsers");
        List<UserGetDTO> users = this.userServiceImpl.getAllUsers().getBody();
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        modelAndView.addObject("users", users);
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    /**
     * Creates a new user in the db, redirects if success to main page
     *
     * @param user
     * @return RedirectView
     */
    @PostMapping("/createUser")
    public RedirectView addUser(@ModelAttribute("user") UserPostDTO user) {
        LOGGER.info("Request for creating a new user");
        this.userServiceImpl.addUser(user);
        return new RedirectView("/listOfUsers");
    }

    /**
     * Gets the page for creating a new account
     *
     * @return ModelAndView
     */
    @GetMapping("/signUp")
    public ModelAndView createUser() {
        ModelAndView modelAndView = new ModelAndView("signUp");
        User user = new User();
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    /**
     * Gets user by id
     *
     * @param id
     * @return ResponseEntity<UserGetDTO>
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<UserGetDTO> getUserById(@PathVariable UUID id) {
        LOGGER.info("Request for a user by id");
        return this.userServiceImpl.getUserById(id);
    }

    /**
     * Gets user profile for auto update or delete
     *
     * @return ModelAndView
     */
    @GetMapping("/userProfile")
    public ModelAndView userProfile() {
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        ModelAndView modelAndView = new ModelAndView("userProfile");
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }


    /**
     * Gets the login page
     *
     * @return ModelAndView
     */
    @GetMapping("/login")
    public ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("login");
        modelAndView.addObject("loginDTO", new LoginDTO());
        return modelAndView;
    }

    /**
     * Logs the user out of the app
     *
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public RedirectView logout() {
        session.invalidate();
        return new RedirectView("/login");
    }

    /**
     * @param loginDTO
     * @return RedirectView
     */
    @PostMapping("/login/user")
    public RedirectView loginUser(@ModelAttribute("loginDTO") LoginDTO loginDTO) {
        session.invalidate();;
        ResponseEntity<UserGetDTO> response = this.userServiceImpl.getUserByEmailAndPassword(loginDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            session.setAttribute("loggedInUser",response.getBody());
            return new RedirectView("/userProfile");
        }
        return new RedirectView("/login");
    }

    /**
     * Updates user by given id and request body
     *
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
     *
     * @param id
     * @return ResponseEntity<String>
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable UUID id) {
        LOGGER.info("Request for deleting a user by id");
        return this.userServiceImpl.deleteUserById(id);
    }

}
