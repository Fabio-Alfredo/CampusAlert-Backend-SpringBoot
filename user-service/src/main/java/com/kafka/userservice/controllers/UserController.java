package com.kafka.userservice.controllers;

import com.kafka.userservice.domain.dtos.commons.GeneralResponse;
import com.kafka.userservice.domain.dtos.user.UpdateRolesDto;
import com.kafka.userservice.domain.dtos.user.UserDto;
import com.kafka.userservice.domain.enums.RolesActions;
import com.kafka.userservice.domain.models.User;
import com.kafka.userservice.services.contract.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/by-id/{userId}")
//    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> getUserById(@PathVariable UUID userId) {
        try {
            var user = userService.findById(userId);
            return GeneralResponse.getResponse(HttpStatus.OK, "User retrieved successfully", new UserDto(user));
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error retrieving user: " + e.getMessage());
        }
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> getAllUsers() {
        try {
            var users = userService.findAllUsers();
            return GeneralResponse.getResponse(HttpStatus.OK, "Users retrieved successfully", users.stream().map(UserDto::new).toList());
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error retrieving users: " + e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<GeneralResponse> getAuthenticatedUser() {
        try {
            User user = userService.getUserAuthenticated();
            return GeneralResponse.getResponse(HttpStatus.OK, "Authenticated user retrieved successfully", new UserDto(user));
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error retrieving authenticated user: " + e.getMessage());
        }
    }

    @GetMapping("/exists/{id}/{role}")
    public ResponseEntity<GeneralResponse> existsUserById(@PathVariable UUID id,
                                                          @PathVariable String role) {
        try {
            boolean exists = userService.existUserByIdAndRole(id, role);
            return GeneralResponse.getResponse(HttpStatus.OK, "User existence checked successfully", exists);
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error checking user existence: " + e.getMessage());
        }
    }

    @PutMapping("/update-role")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<GeneralResponse> updateUserRole(@RequestBody @Valid UpdateRolesDto data) {
        try {
            RolesActions rolesActions =RolesActions.fromString(data.getAction());
            userService.updateRole(data.getUserId(), data.getRoleId(), rolesActions);
            return GeneralResponse.getResponse(HttpStatus.OK, "User role updated successfully");
        } catch (Exception e) {
            return GeneralResponse.getResponse(HttpStatus.BAD_REQUEST, "Error updating user role: " + e.getMessage());
        }
    }
}
