package com.security_app.controller;

import jakarta.validation.Valid;
import com.security_app.model_entity.user_dtos.UserDto;
import com.security_app.model_entity.user_dtos.UserRegistrationDto;
import com.security_app.model_entity.user_dtos.UserUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.security_app.service.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.registerUser(registrationDto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCurrentUser(#id)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtil.isCurrentUser(#id)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @Valid @RequestBody UserUpdateDto userUpdateDto) {
        return ResponseEntity.ok(userService.updateUser(id, userUpdateDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/roles")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> changeUserRole(
            @PathVariable Long id,
            @RequestParam String role,
            @RequestParam boolean add) {
        userService.changeUserRole(id, role, add);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> toggleUserStatus(@PathVariable Long id) {
        userService.toggleUserStatus(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/login")
    public ResponseEntity<UserDto> authenticateUser(
            @RequestParam String username,
            @RequestParam String password) {

        // Log authentication attempt (optional)
//        logger.info("Authentication attempt for username: {}", username);

        Optional<UserDto> authenticatedUser = userService.getUser(username, password);

        return authenticatedUser
                .map(userDto -> {
//                    logger.info("User authenticated successfully: {}", username);
                    return ResponseEntity.ok(userDto);
                })
                .orElseGet(() -> {
//                    logger.warn("Authentication failed for username: {}", username);
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
                });
    }
}
