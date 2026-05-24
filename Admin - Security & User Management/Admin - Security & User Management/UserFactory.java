package com.sliit.registration.factory;

import com.sliit.registration.model.*;

/**
 * Factory Design Pattern to centralize User creation.
 * Decouples the service layer from concrete implementations.
 */
public class UserFactory {

    public static User createUser(String role) {
        if (role == null || role.isBlank()) return null;
        
        String cleanRole = role.trim().toUpperCase();
        return switch (cleanRole) {
            case "STUDENT" -> new Student();
            case "ADMIN" -> new Admin();
            case "MODERATOR" -> new Moderator();
            default -> null; // Return null instead of throwing exception for safer parsing
        };
    }

    public static User createUser(String userId, String username, String password, String role, String extra) {
        if (role == null || role.isBlank()) return null;

        String cleanRole = role.trim().toUpperCase();
        return switch (cleanRole) {
            case "STUDENT" -> new Student(userId, username, password, 0.0, 0);
            case "ADMIN" -> new Admin(userId, username, password, extra != null ? extra : "1");
            case "MODERATOR" -> new Moderator(userId, username, password, extra != null ? extra : "General");
            default -> throw new IllegalArgumentException("Unknown user role: " + role);
        };
    }
}
