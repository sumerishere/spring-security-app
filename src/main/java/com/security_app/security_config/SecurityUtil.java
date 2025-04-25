package com.security_app.security_config;

import com.security_app.model_entity.User;
import com.security_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtil {

    @Autowired
    private UserRepository userRepository;

    public boolean isCurrentUser(Long userId) {
        try {
            // Get current authentication
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication == null || !authentication.isAuthenticated()) {
                return false;
            }

            // Get the current authenticated username
            String currentUsername = authentication.getName();

            // Find user by username
            Optional<User> currentUser = userRepository.findByUsername(currentUsername);
            if (currentUser.isEmpty()) {
                System.out.println("Could not find user with username: " + currentUsername);
                return false;
            }

            // Compare user IDs directly
            boolean isAuthorized = currentUser.get().getId().equals(userId);

            System.out.println("------------------securityUtil Logs---------------------------");
            System.out.println("Current authenticated username: " + currentUsername);
            System.out.println("Current user ID: " + currentUser.get().getId());
            System.out.println("Requested user ID: " + userId);
            System.out.println("Access authorized: " + isAuthorized);
            System.out.println("-------------------------------------------------------------");

            return isAuthorized;

        } catch (Exception e) {
            System.out.println("Error in security check: " + e.getMessage());
            e.printStackTrace();
            return false; // Default to denying access on errors
        }
    }
}