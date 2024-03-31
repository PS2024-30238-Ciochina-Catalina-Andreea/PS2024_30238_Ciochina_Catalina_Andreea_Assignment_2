package com.example.flowerShop.controller;

import com.example.flowerShop.dto.user.LoginDTO;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.service.impl.UserServiceImpl;
import com.example.flowerShop.utils.user.UserRole;
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
@RequestMapping()
@CrossOrigin("*")
public class UserController {

    private final UserServiceImpl userServiceImpl;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private HttpSession session;

    /**
     * Injected constructor
     *
     * @param userServiceImpl
     */
    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    /**
     * Gets list of users for admin only
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
     * Gets the page for creating a new account
     *
     * @return ModelAndView
     */
    @GetMapping("/signUp")
    public ModelAndView createUser() {
        ModelAndView modelAndView = new ModelAndView("signUp");
        modelAndView.addObject("user", new User());
        return modelAndView;
    }

    /**
     * Creates a new user in the db, redirects if success to login page, else back to signUp
     *
     * @param user
     * @return RedirectView
     */
    @PostMapping("/createUser")
    public RedirectView addUser(@ModelAttribute("user") UserPostDTO user) {
        LOGGER.info("Request for creating a new user");
        ResponseEntity<String> stringResponseEntity = this.userServiceImpl.addUser(user);
        if (stringResponseEntity.getStatusCode() == HttpStatus.CREATED) {
            return new RedirectView("/login");
        }
        return new RedirectView("/signUp");
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
     * Gets user profile page for auto update or delete
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
     * Logs the user in and redirects in positive case to list of products, else back to login
     *
     * @param loginDTO
     * @return RedirectView
     */
    @PostMapping("/login/user")
    public RedirectView loginUser(@ModelAttribute("loginDTO") LoginDTO loginDTO) {
        session.invalidate();
        ResponseEntity<UserGetDTO> response = this.userServiceImpl.getUserByEmailAndPassword(loginDTO);
        if (response.getStatusCode() == HttpStatus.OK) {
            session.setAttribute("loggedInUser", response.getBody());
            return new RedirectView("/createCart");
        }
        return new RedirectView("/login");
    }

    /**
     * Logs the user out of the app and redirects to login page
     *
     * @return RedirectView
     */
    @GetMapping("/logout")
    public RedirectView logout() {
        session.invalidate();
        return new RedirectView("/login");
    }

    /**
     * Gets the page for creating a new account
     *
     * @return ModelAndView
     */
    @GetMapping("/updateUser/{id}")
    public ModelAndView updateUser(@PathVariable UUID id) {
        ModelAndView modelAndView = new ModelAndView("updateUser");
        User user = this.userServiceImpl.convertToModelObject(id);
        modelAndView.addObject("user", user);
        return modelAndView;
    }

    /**
     * Updates user by given id and request body
     *
     * @param id
     * @param user
     * @return ResponseEntity<String>
     */
    @PostMapping("/update/{id}")
    public RedirectView updateUserById(@PathVariable UUID id, @ModelAttribute("user") @RequestBody UserPostDTO user) {
        LOGGER.info("Request for updating data for a user by id");
        ResponseEntity<String> response = this.userServiceImpl.updateUserById(id, user);
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        if (response.getStatusCode() == HttpStatus.OK) {
            if(currentUser.getRole().equals(UserRole.ADMIN)) {
                return new RedirectView("/listOfUsers");
            }
            session.setAttribute("loggedInUser", getUserById(user.getId()).getBody());
            return new RedirectView("/userProfile");
        }
        return new RedirectView("/updateUser/"+id);
    }

    /**
     * Deletes user by given id and redirects to login
     *
     * @param id
     * @return RedirectView
     */
    @PostMapping("/delete/{id}")
    public RedirectView deleteUserById(@PathVariable UUID id) {
        LOGGER.info("Request for deleting a user by id");
        UserGetDTO currentUser = (UserGetDTO) session.getAttribute("loggedInUser");
        this.userServiceImpl.deleteUserById(id);
        if(currentUser.getRole().equals(UserRole.CUSTOMER)) {
            session.invalidate();
            return new RedirectView("/login");
        }
        return new RedirectView("/listOfUsers");
    }
}
