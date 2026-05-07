package com.university.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.university.auth.AuthService;
import com.university.exception.DuplicateStudentException;
import com.university.exception.StudentNotFoundException;
import com.university.model.Session;
import com.university.model.Student;
import com.university.service.StudentService;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.Logger;

/**
 * StudentController — REST API + static file server.
 *
 * All /api/students endpoints require a valid session token
 * passed as:  Authorization: Bearer <token>
 */
public class StudentController {

    private static final Logger LOG = Logger.getLogger(StudentController.class.getName());
    public  static final int    PORT = 8080;

    private final StudentService      service;
    private final AuthService         authService;
    private       HttpServer          httpServer;

    public StudentController(StudentService service, AuthService authService) {
        this.service     = service;
        this.authService = authService;
    }

    public void start() throws IOException {
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);

        // Auth routes (no auth required)
        httpServer.createContext("/api/auth",      new AuthController(authService));

        // Protected API routes
        httpServer.createContext("/api/students",  new StudentsHandler());
        httpServer.createContext("/api/stats",     new StatsHandler());

        // Database viewer (admin only)
        httpServer.createContext("/api/db-viewer", new DatabaseViewerController(service.getRepository(), authService));

        // Static files (login.html, index.html)
        httpServer.createContext("/", new StaticHandler());

        httpServer.setExecutor(null);
        httpServer.start();
        LOG.info("╔═══════════════════════════════════════════╗");
        LOG.info("║   UniPortal running on http://localhost:" + PORT + "  ║");
        LOG.info("║   Default login:  admin / admin123        ║");
        LOG.info("╚═══════════════════════════════════════════╝");
    }

    public void stop() { if (httpServer != null) httpServer.stop(0); }

    // ── Static files ──────────────────────────────────────────────────────────
    private static class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            String path = ex.getRequestURI().getPath();
            if (path.equals("/") || path.equals("/index.html")) path = "/index.html";
            if (path.equals("/login") || path.equals("/login.html")) path = "/login.html";
            if (path.equals("/database") || path.equals("/database.html")) path = "/database.html";

            InputStream is = getClass().getResourceAsStream("/static" + path);
            if (is == null) is = getClass().getResourceAsStream("/static/index.html");
            if (is == null) { sendRaw(ex, 404, "text/plain", "Not found"); return; }

            byte[] bytes = is.readAllBytes();
            ex.getResponseHeaders().set("Content-Type", contentType(path));
            ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
            ex.sendResponseHeaders(200, bytes.length);
            ex.getResponseBody().write(bytes);
            ex.getResponseBody().close();
        }
        private String contentType(String p) {
            if (p.endsWith(".html")) return "text/html; charset=utf-8";
            if (p.endsWith(".css"))  return "text/css; charset=utf-8";
            if (p.endsWith(".js"))   return "application/javascript; charset=utf-8";
            return "application/octet-stream";
        }
        private void sendRaw(HttpExchange ex, int code, String ct, String body) throws IOException {
            byte[] b = body.getBytes(StandardCharsets.UTF_8);
            ex.getResponseHeaders().set("Content-Type", ct);
            ex.sendResponseHeaders(code, b.length);
            ex.getResponseBody().write(b); ex.getResponseBody().close();
        }
    }

    // ── Stats ─────────────────────────────────────────────────────────────────
    private class StatsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            cors(ex);
            if ("OPTIONS".equals(ex.getRequestMethod())) { send(ex, 204, ""); return; }
            if (!authenticated(ex)) return;
            if (!"GET".equals(ex.getRequestMethod())) { send(ex, 405, JsonUtil.error("Method not allowed")); return; }
            send(ex, 200, JsonUtil.toJson(service.getDashboardStats()));
        }
    }

    // ── Students ──────────────────────────────────────────────────────────────
    private class StudentsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange ex) throws IOException {
            cors(ex);
            if ("OPTIONS".equals(ex.getRequestMethod())) { send(ex, 204, ""); return; }
            if (!authenticated(ex)) return;

            try {
                String[] parts  = ex.getRequestURI().getPath().split("/");
                String   method = ex.getRequestMethod();
                String   id     = (parts.length == 4) ? parts[3] : null;

                if ("GET".equals(method) && id == null) {
                    String q    = qp(ex.getRequestURI(), "q");
                    String dept = qp(ex.getRequestURI(), "dept");
                    List<Student> list = q != null && !q.isBlank() ? service.searchStudents(q)
                                      : dept != null && !dept.isBlank() ? service.getStudentsByDepartment(dept)
                                      : service.getAllStudents();
                    send(ex, 200, JsonUtil.toJsonArray(list));
                } else if ("GET".equals(method) && id != null) {
                    send(ex, 200, JsonUtil.toJson(service.getStudentById(id)));
                } else if ("POST".equals(method) && id == null) {
                    Map<String, String> b = parseBody(ex);
                    Student s = service.createStudent(b.get("firstName"), b.get("lastName"),
                        b.get("email"), b.get("phone"), b.get("department"), b.get("year"),
                        Double.parseDouble(b.getOrDefault("gpa", "0.0")));
                    send(ex, 201, JsonUtil.toJson(s));
                } else if ("PUT".equals(method) && id != null) {
                    send(ex, 200, JsonUtil.toJson(service.updateStudent(id, parseBody(ex))));
                } else if ("DELETE".equals(method) && id != null) {
                    service.deleteStudent(id);
                    send(ex, 200, JsonUtil.success("Student deleted successfully."));
                } else {
                    send(ex, 404, JsonUtil.error("Route not found"));
                }
            } catch (StudentNotFoundException e)  { send(ex, 404, JsonUtil.error(e.getMessage())); }
              catch (DuplicateStudentException e)  { send(ex, 409, JsonUtil.error(e.getMessage())); }
              catch (IllegalArgumentException e)   { send(ex, 400, JsonUtil.error(e.getMessage())); }
              catch (Exception e) {
                LOG.severe("Error: " + e.getMessage());
                send(ex, 500, JsonUtil.error("Internal server error"));
              }
        }
    }

    // ── Auth guard ────────────────────────────────────────────────────────────
    private boolean authenticated(HttpExchange ex) throws IOException {
        String token = AuthController.extractToken(ex);
        if (authService.validate(token).isPresent()) return true;
        send(ex, 401, JsonUtil.error("Authentication required. Please log in."));
        return false;
    }

    // ── Utilities ─────────────────────────────────────────────────────────────
    private static void cors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        ex.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    private static void send(HttpExchange ex, int code, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        ex.sendResponseHeaders(code, bytes.length);
        OutputStream os = ex.getResponseBody();
        os.write(bytes); os.close();
    }

    private static Map<String, String> parseBody(HttpExchange ex) throws IOException {
        String raw = new String(ex.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> map = new LinkedHashMap<>();
        raw = raw.trim().replaceAll("^\\{|\\}$", "");
        for (String pair : raw.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)")) {
            String[] kv = pair.split(":", 2);
            if (kv.length == 2) {
                map.put(kv[0].trim().replaceAll("\"", ""), kv[1].trim().replaceAll("^\"|\"$", ""));
            }
        }
        return map;
    }

    private static String qp(URI uri, String name) {
        if (uri.getQuery() == null) return null;
        for (String p : uri.getQuery().split("&")) {
            String[] kv = p.split("=", 2);
            if (kv.length == 2 && kv[0].equals(name)) return kv[1];
        }
        return null;
    }
}
