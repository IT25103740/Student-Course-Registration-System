package com.university.auth;

import com.university.model.Session;
import com.university.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * AuthService — manages users and login sessions.
 *
 * OOP: Single Responsibility, Encapsulation, Dependency-free service.
 *
 * Default accounts (created on startup):
 *   admin   / admin123   → ADMIN  (full access)
 *   staff   / staff123   → STAFF  (read + write, no delete)
 *   viewer  / viewer123  → VIEWER (read only)
 */
public class AuthService {

    private final Map<String, User>    users    = new ConcurrentHashMap<>(); // username → User
    private final Map<String, Session> sessions = new ConcurrentHashMap<>(); // token → Session

    // ── Boot: seed default accounts ──────────────────────────────────────────
    public AuthService() {
        addUser(User.create("admin",  "admin123",  "Administrator",  "admin@uni.lk",  User.Role.ADMIN));
        addUser(User.create("staff",  "staff123",  "Staff Member",   "staff@uni.lk",  User.Role.STAFF));
        addUser(User.create("viewer", "viewer123", "View Only User", "viewer@uni.lk", User.Role.VIEWER));
    }

    private void addUser(User u) { users.put(u.getUsername(), u); }

    // ── Login ────────────────────────────────────────────────────────────────
    /**
     * Attempt login. Returns a new Session token on success.
     * @throws SecurityException on bad credentials.
     */
    public Session login(String username, String password) {
        User user = users.get(username);
        if (user == null || !user.isActive()) {
            throw new SecurityException("Invalid username or password.");
        }
        if (!user.verifyPassword(password)) {
            throw new SecurityException("Invalid username or password.");
        }
        Session session = new Session(user.getUserId(), user.getUsername(), user.getRole());
        sessions.put(session.getToken(), session);
        return session;
    }

    // ── Logout ───────────────────────────────────────────────────────────────
    public void logout(String token) {
        sessions.remove(token);
    }

    // ── Validate session ─────────────────────────────────────────────────────
    public Optional<Session> validate(String token) {
        if (token == null || token.isBlank()) return Optional.empty();
        Session s = sessions.get(token);
        if (s == null || s.isExpired()) {
            sessions.remove(token);
            return Optional.empty();
        }
        return Optional.of(s);
    }

    // ── Get all users (for display) ──────────────────────────────────────────
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    // ── Purge expired sessions ────────────────────────────────────────────────
    public void purgeExpired() {
        sessions.entrySet().removeIf(e -> e.getValue().isExpired());
    }
}
