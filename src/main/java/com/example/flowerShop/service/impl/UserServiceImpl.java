package com.example.flowerShop.service.impl;

import com.example.flowerShop.constants.UserConstants;
import com.example.flowerShop.dto.user.UserGetDTO;
import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.mapper.UserMapper;
import com.example.flowerShop.entity.User;
import com.example.flowerShop.repository.UserRepository;
import com.example.flowerShop.service.UserService;
import com.example.flowerShop.utils.Utils;
import com.example.flowerShop.utils.user.UserUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserUtils userUtils;

    private final UserMapper userMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Injected constructor
     * @param userRepository
     * @param userUtils
     * @param userMapper
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserUtils userUtils, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userUtils = userUtils;
        this.userMapper = userMapper;
    }

    /**
     * Gets a list of user entries from the db
     * @return ResponseEntity<List<UserGetDTO>>
     */
    @Override
    public ResponseEntity<List<UserGetDTO>> getAllUsers() {

        LOGGER.info("Fetching users list...");
        try {
            List<User> users = userRepository.findAll();
            LOGGER.info("Fetching completed, list of users retrieved");
            return new ResponseEntity<>(userMapper.convertListToDTO(users), HttpStatus.OK);
        } catch (Exception exception) {
            LOGGER.error("Error while performing the fetching of the list with users", exception);
            exception.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Gets an existing user by id
     * @param id
     * @return ResponseEntity<UserGetDTO>
     */
    @Override
    public ResponseEntity<UserGetDTO> getUserById(UUID id) {

        LOGGER.info("Fetching user with id = " + id);
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User userExisting = userOptional.get();
                LOGGER.info("Fetching completed, user retrieved");
                return new ResponseEntity<>(userMapper.convertToDTO(userExisting), HttpStatus.OK);
            } else {
                LOGGER.error("User with id = {} not found in the db", id);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            LOGGER.error("Error while retrieving the user by id");
            exception.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Creates a new user in the user table
     * @param user
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> addUser(UserPostDTO user) {

        LOGGER.info("Creating a new user...");
        try {
            if (this.userUtils.validateSignUpMap(user)) {
                Optional<User> userOptional = userRepository.findByEmail(user.getEmail());
                if (userOptional.isEmpty()) {
                    LOGGER.info("User created");
                    userRepository.save(userMapper.convertToEntity(user));
                    return Utils.getResponseEntity(UserConstants.USER_CREATED, HttpStatus.CREATED);
                } else {
                    LOGGER.error("User already exists, email is present in the db");
                    return Utils.getResponseEntity(UserConstants.USER_EXISTS, HttpStatus.BAD_REQUEST);
                }
            } else {
                LOGGER.error("Invalid data was sent for creating the user");
                return Utils.getResponseEntity(UserConstants.INVALID_DATA_AT_CREATING_USER, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at creating the user");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG_AT_CREATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Updates an existing user from the user table with new values
     * @param id
     * @param user
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> updateUserById(UUID id, UserPostDTO user) {

        LOGGER.info("Updating the data for a user with id {}...", id);
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                User userExisting = userOptional.get();
                UserUtils.updateUserValues(userExisting, user);
                LOGGER.info("Completed user update");
                userRepository.save(userExisting);
                return Utils.getResponseEntity(UserConstants.DATA_MODIFIED, HttpStatus.OK);
            } else {
                LOGGER.error("User with id = {} not found in the db", id);
                return Utils.getResponseEntity(UserConstants.INVALID_USER, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception exception) {
            LOGGER.error("Something went wrong at updating the user");
            exception.printStackTrace();
        }
        return Utils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG_AT_UPDATING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Deletes an existing user by given id
     * @param id
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteUserById(UUID id) {

        LOGGER.info("Deleting the user with id {}...", id);
        try {
            Optional<User> userOptional = userRepository.findById(id);
            if (userOptional.isPresent()) {
                userRepository.deleteById(id);
                LOGGER.info("User deleted successfully");
                return Utils.getResponseEntity(UserConstants.USER_DELETED, HttpStatus.OK);
            } else {
                LOGGER.error("User with id = {} not found in the db", id);
                return Utils.getResponseEntity(UserConstants.INVALID_USER, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            LOGGER.error("Something went wrong at deleting the user");
            e.printStackTrace();
        }
        return Utils.getResponseEntity(UserConstants.SOMETHING_WENT_WRONG_AT_DELETING_USER, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
