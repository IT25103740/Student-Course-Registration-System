package com.university.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * User — represents an admin/staff account that can log in.
 *
 * OOP: Encapsulation, password hashing, factory methods.
 */
public class User {

    public enum Role { ADMIN, STAFF, VIEWER }

    private final String userId;
    private String username;
    private String passwordHash;
    private String salt;
    private String fullName;
    private String email;
    private Role   role;
    private boolean active;

    // ── Constructor ──────────────────────────────────────────────────────────
    public User(String userId, String username, String passwordHash,
                String salt, String fullName, String email,
                Role role, boolean active) {
        this.userId       = userId;
        this.username     = username;
        this.passwordHash = passwordHash;
        this.salt         = salt;
        this.fullName     = fullName;
        this.email        = email;
        this.role         = role;
        this.active       = active;
    }

    // ── Factory — creates a new user and hashes the password ────────────────
    public static User create(String username, String plainPassword,
                               String fullName, String email, Role role) {
        String userId = "USR-" + System.currentTimeMillis();
        String salt   = generateSalt();
        String hash   = hashPassword(plainPassword, salt);
        return new User(userId, username, hash, salt, fullName, email, role, true);
    }

    // ── Password verification ────────────────────────────────────────────────
    public boolean verifyPassword(String plainPassword) {
        return hashPassword(plainPassword, this.salt).equals(this.passwordHash);
    }

    // ── Static helpers ───────────────────────────────────────────────────────
    private static String generateSalt() {
        byte[] bytes = new byte[16];
        new SecureRandom().nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((salt + password).getBytes());
            return Base64.getEncoder().encodeToString(md.digest());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public String  getUserId()      { return userId; }
    public String  getUsername()    { return username; }
    public String  getPasswordHash(){ return passwordHash; }
    public String  getSalt()        { return salt; }
    public String  getFullName()    { return fullName; }
    public String  getEmail()       { return email; }
    public Role    getRole()        { return role; }
    public boolean isActive()       { return active; }

    public void setActive(boolean active) { this.active = active; }
    public void setRole(Role role)        { this.role = role; }
}
