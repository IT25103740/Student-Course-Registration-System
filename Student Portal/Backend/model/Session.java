package com.university.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Session — represents an authenticated browser session.
 * Created on login, destroyed on logout or expiry.
 */
public class Session {

    private static final int SESSION_HOURS = 8;

    private final String        token;
    private final String        userId;
    private final String        username;
    private final User.Role     role;
    private final LocalDateTime createdAt;
    private final LocalDateTime expiresAt;

    public Session(String userId, String username, User.Role role) {
        this.token     = UUID.randomUUID().toString();
        this.userId    = userId;
        this.username  = username;
        this.role      = role;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = createdAt.plusHours(SESSION_HOURS);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }

    public String      getToken()    { return token; }
    public String      getUserId()   { return userId; }
    public String      getUsername() { return username; }
    public User.Role   getRole()     { return role; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiresAt() { return expiresAt; }
}
