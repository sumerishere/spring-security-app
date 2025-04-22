package com.security_app.security_config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        // Get the current username
        String currentUsername = authentication.getName();

        // In a real application, you would fetch the user with this ID and compare usernames
        // This is a simplified example
        return true; // Implement proper logic based on your application's user details
    }
}
