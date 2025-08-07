package com.kafka.userservice.controllers;

import com.kafka.userservice.domain.dtos.commons.GeneralResponse;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.services.contract.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/by-id/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> getUserById(@PathVariable UUID userId) {
        try {
            var user = userService.findById(userId);
            return GeneralResponse.getResponse(HttpStatus.OK, "User retrieved successfully", user);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error retrieving user: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> getAllUsers() {
        try {
            var users = userService.findAllUsers();
            return GeneralResponse.getResponse(HttpStatus.OK, "Users retrieved successfully", users);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error retrieving users: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<GeneralResponse> getAuthenticatedUser() {
        try {
            User user = userService.getUserAuthenticated();
            return GeneralResponse.getResponse(HttpStatus.OK, "Authenticated user retrieved successfully", user);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error retrieving authenticated user: " + e.getMessage());
        }
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<GeneralResponse> existsUserById(@PathVariable UUID id) {
        try {
            boolean exists = userService.existUserById(id);
            return GeneralResponse.getResponse(HttpStatus.OK, "User existence checked successfully", exists);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error checking user existence: " + e.getMessage());
        }
    }
}
