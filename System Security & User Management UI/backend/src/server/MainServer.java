package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.FileDatabase;
import models.AuthLog;
import models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class MainServer {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/users", new UsersHandler());
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/logs", new LogsHandler());
        server.setExecutor(null);
        System.out.println("Java I/O Backend Server running on http://localhost:8080");
        server.start();
    }

    private static void setCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static Map<String, String> parseFormData(String formData) {
        Map<String, String> map = new HashMap<>();
        if (formData == null || formData.isEmpty()) return map;
        String[] pairs = formData.split("&");
        for (String pair : pairs) {
            String[] kv = pair.split("=");
            if (kv.length == 2) {
                map.put(kv[0], kv[1]);
            }
        }
        return map;
    }

    private static String getBody(HttpExchange exchange) throws IOException {
        InputStream is = exchange.getRequestBody();
        Scanner scanner = new Scanner(is, "UTF-8").useDelimiter("\\A");
        return scanner.hasNext() ? scanner.next() : "";
    }

    private static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes("UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        OutputStream os = exchange.getResponseBody();
        os.write(bytes);
        os.close();
    }

    static class UsersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.getResponseBody().close();
                return;
            }

            List<User> users = FileDatabase.loadUsers();

            if ("GET".equals(exchange.getRequestMethod())) {
                // Return all users as JSON array
                String json = "[" + users.stream().map(User::toJson).collect(Collectors.joining(",")) + "]";
                sendResponse(exchange, 200, json);

            } else if ("POST".equals(exchange.getRequestMethod())) {
                // Register new user
                Map<String, String> data = parseFormData(getBody(exchange));
                String username = data.get("username");
                String password = data.get("password");
                String role = data.getOrDefault("role", "STUDENT");

                if (username == null || password == null) {
                    sendResponse(exchange, 400, "{\"error\":\"Missing fields\"}");
                    return;
                }

                if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
                    sendResponse(exchange, 400, "{\"error\":\"Username exists\"}");
                    return;
                }

                User newUser = new User(UUID.randomUUID().toString(), username, password, role, true);
                users.add(newUser);
                FileDatabase.saveUsers(users);

                logAction(username, "REGISTER_ACCOUNT");
                sendResponse(exchange, 201, newUser.toJson());

            } else if ("PUT".equals(exchange.getRequestMethod())) {
                // Update user
                Map<String, String> data = parseFormData(getBody(exchange));
                String id = data.get("id");
                String newPassword = data.get("password");
                String newRole = data.get("role");

                Optional<User> optUser = users.stream().filter(u -> u.getId().equals(id)).findFirst();
                if (optUser.isPresent()) {
                    User u = optUser.get();
                    if (newPassword != null && !newPassword.isEmpty()) u.setPassword(newPassword);
                    if (newRole != null && !newRole.isEmpty()) u.setRole(newRole);
                    FileDatabase.saveUsers(users);
                    logAction(u.getUsername(), "UPDATE_PROFILE");
                    sendResponse(exchange, 200, u.toJson());
                } else {
                    sendResponse(exchange, 404, "{\"error\":\"User not found\"}");
                }

            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                // Soft delete user
                Map<String, String> data = parseFormData(getBody(exchange));
                String id = data.get("id");

                Optional<User> optUser = users.stream().filter(u -> u.getId().equals(id)).findFirst();
                if (optUser.isPresent()) {
                    User u = optUser.get();
                    u.setActive(false); // Soft delete
                    FileDatabase.saveUsers(users);
                    logAction(u.getUsername(), "ACCOUNT_DISABLED");
                    sendResponse(exchange, 200, "{\"message\":\"User deleted\"}");
                } else {
                    sendResponse(exchange, 404, "{\"error\":\"User not found\"}");
                }
            }
        }
    }

    static class LoginHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.getResponseBody().close();
                return;
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                Map<String, String> data = parseFormData(getBody(exchange));
                String username = data.get("username");
                String password = data.get("password");

                List<User> users = FileDatabase.loadUsers();
                Optional<User> optUser = users.stream()
                        .filter(u -> u.getUsername().equals(username) && u.getPassword().equals(password))
                        .findFirst();

                if (optUser.isPresent() && optUser.get().isActive()) {
                    logAction(username, "LOGIN_SUCCESS");
                    sendResponse(exchange, 200, optUser.get().toJson());
                } else {
                    logAction(username != null ? username : "UNKNOWN", "LOGIN_FAILED");
                    sendResponse(exchange, 401, "{\"error\":\"Invalid credentials or disabled account\"}");
                }
            }
        }
    }

    static class LogsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCorsHeaders(exchange);
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                exchange.getResponseBody().close();
                return;
            }

            if ("GET".equals(exchange.getRequestMethod())) {
                List<AuthLog> logs = FileDatabase.loadLogs();
                String json = "[" + logs.stream().map(AuthLog::toJson).collect(Collectors.joining(",")) + "]";
                sendResponse(exchange, 200, json);
            } else if ("DELETE".equals(exchange.getRequestMethod())) {
                FileDatabase.clearLogs();
                sendResponse(exchange, 200, "{\"message\":\"Logs cleared\"}");
            }
        }
    }

    private static void logAction(String username, String action) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        AuthLog log = new AuthLog(UUID.randomUUID().toString(), username, action, timestamp);
        FileDatabase.addLog(log);
    }
}
