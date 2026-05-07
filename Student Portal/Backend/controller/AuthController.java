package com.university.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.university.auth.AuthService;
import com.university.model.Session;
import com.university.model.User;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

/**
 * AuthController — handles /api/auth/* endpoints.
 *
 * POST /api/auth/login   → { username, password } → { token, role, username }
 * POST /api/auth/logout  → clears session
 * GET  /api/auth/me      → returns current session info
 */
public class AuthController implements HttpHandler {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        cors(ex);
        if ("OPTIONS".equals(ex.getRequestMethod())) { send(ex, 204, ""); return; }

        String path = ex.getRequestURI().getPath(); // /api/auth/login etc.

        try {
            if (path.endsWith("/login") && "POST".equals(ex.getRequestMethod())) {
                handleLogin(ex);
            } else if (path.endsWith("/logout") && "POST".equals(ex.getRequestMethod())) {
                handleLogout(ex);
            } else if (path.endsWith("/me") && "GET".equals(ex.getRequestMethod())) {
                handleMe(ex);
            } else {
                send(ex, 404, JsonUtil.error("Auth route not found"));
            }
        } catch (SecurityException e) {
            send(ex, 401, JsonUtil.error(e.getMessage()));
        } catch (Exception e) {
            send(ex, 500, JsonUtil.error("Internal error"));
        }
    }

    private void handleLogin(HttpExchange ex) throws IOException {
        Map<String, String> body = parseBody(ex);
        String username = body.getOrDefault("username", "").trim();
        String password = body.getOrDefault("password", "").trim();

        if (username.isEmpty() || password.isEmpty()) {
            send(ex, 400, JsonUtil.error("Username and password are required."));
            return;
        }

        Session session = authService.login(username, password);
        String json = "{\"token\":\"" + session.getToken() + "\","
                    + "\"username\":\"" + session.getUsername() + "\","
                    + "\"role\":\"" + session.getRole().name() + "\","
                    + "\"expiresAt\":\"" + session.getExpiresAt() + "\"}";
        send(ex, 200, json);
    }

    private void handleLogout(HttpExchange ex) throws IOException {
        String token = extractToken(ex);
        authService.logout(token);
        send(ex, 200, JsonUtil.success("Logged out successfully."));
    }

    private void handleMe(HttpExchange ex) throws IOException {
        String token = extractToken(ex);
        Optional<Session> session = authService.validate(token);
        if (session.isEmpty()) {
            send(ex, 401, JsonUtil.error("Not authenticated."));
            return;
        }
        Session s = session.get();
        String json = "{\"username\":\"" + s.getUsername() + "\","
                    + "\"role\":\"" + s.getRole().name() + "\"}";
        send(ex, 200, json);
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Extract Bearer token from Authorization header */
    public static String extractToken(HttpExchange ex) {
        String auth = ex.getRequestHeaders().getFirst("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) return auth.substring(7).trim();
        return "";
    }

    private static Map<String, String> parseBody(HttpExchange ex) throws IOException {
        String raw = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new java.util.LinkedHashMap<>();
        raw = raw.trim().replaceAll("^\\{|\\}$", "");
        for (String pair : raw.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                String key   = kv[0].trim().replaceAll("\"", "");
                String value = kv[1].trim().replaceAll("^\"|\"$", "");
                map.put(key, value);
            }
        }
        return map;
    }

    private static void cors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(code, bytes.length);
        OutputStream os = ex.getResponseBody();
        os.write(bytes);
        os.close();
    }
}
