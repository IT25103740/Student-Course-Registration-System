package com.sliit.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base abstract class demonstrating Inheritance and Polymorphism.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class User implements FilePersistable {
    private String userId;
    private String username;
    private String passwordHash;
    private String role; // STUDENT, MODERATOR, ADMIN

    // Safe placeholders for polymorphic template rendering to prevent 500 errors
    public String getDepartmentId() { return "N/A"; }
    public String getClearanceLevel() { return "N/A"; }

    // Polymorphism: abstract methods to be implemented by child classes
    public abstract String getDashboardUrl();
    public abstract String[] getPermissions();
}
