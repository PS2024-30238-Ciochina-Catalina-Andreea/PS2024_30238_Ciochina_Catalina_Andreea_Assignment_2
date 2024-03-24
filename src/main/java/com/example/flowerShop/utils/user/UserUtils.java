package com.example.flowerShop.utils.user;

import com.example.flowerShop.dto.user.UserPostDTO;
import com.example.flowerShop.entity.User;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@NoArgsConstructor
public class UserUtils {

    public boolean validateSignUpMap(UserPostDTO user) {
        return !Objects.equals(user.getName(), "")
                && !Objects.equals(user.getContactNumber(), "")
                && !Objects.equals(user.getAddress(), "")
                && !Objects.equals(user.getRole(), "")
                && !Objects.equals(user.getPassword(), "")
                && !Objects.equals(user.getEmail(), "");
    }


    public static void updateUserValues(User userExisting, UserPostDTO user) {
        if (Objects.nonNull(user.getName()) && !"".equalsIgnoreCase(user.getName())) {
            userExisting.setName(user.getName());
        }
        if (Objects.nonNull(user.getAddress()) && !"".equalsIgnoreCase(user.getAddress())) {
            userExisting.setAddress(user.getAddress());
        }
        if (Objects.nonNull(user.getContactNumber()) && !"".equalsIgnoreCase(user.getContactNumber())) {
            userExisting.setContactNumber(user.getContactNumber());
        }
        if (Objects.nonNull(user.getEmail()) && !"".equalsIgnoreCase(user.getEmail())) {
            userExisting.setEmail(user.getEmail());
        }
        if (Objects.nonNull(user.getPassword()) && !"".equalsIgnoreCase(user.getPassword())) {
            userExisting.setPassword(user.getPassword());
        }
        if (Objects.nonNull(user.getRole())) {
            userExisting.setRole(user.getRole());
        }
    }
}
