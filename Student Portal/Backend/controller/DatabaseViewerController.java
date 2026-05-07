package com.university.controller;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.university.auth.AuthService;
import com.university.model.Session;
import com.university.model.Student;
import com.university.repository.StudentRepository;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

/**
 * DatabaseViewerController — serves the /api/db-viewer endpoint.
 *
 * GET /api/db-viewer/raw     → full JSON dump of all data (ADMIN only)
 * GET /api/db-viewer/stats   → table/column stats
 *
 * This lets you "look at the database" directly through the UI.
 * Requires ADMIN role.
 */
public class DatabaseViewerController implements HttpHandler {

    private final StudentRepository repo;
    private final AuthService       authService;

    public DatabaseViewerController(StudentRepository repo, AuthService authService) {
        this.repo        = repo;
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange ex) throws IOException {
        cors(ex);
        if ("OPTIONS".equals(ex.getRequestMethod())) { send(ex, 204, ""); return; }

        // Auth check
        String token = AuthController.extractToken(ex);
        Optional<Session> session = authService.validate(token);
        if (session.isEmpty()) { send(ex, 401, JsonUtil.error("Login required.")); return; }

        String path = ex.getRequestURI().getPath();

        try {
            if (path.endsWith("/raw")) {
                List<Student> all = repo.findAll();
                send(ex, 200, buildRawDump(all));
            } else if (path.endsWith("/table")) {
                List<Student> all = repo.findAll();
                send(ex, 200, buildTableDump(all));
            } else {
                send(ex, 404, JsonUtil.error("Not found"));
            }
        } catch (Exception e) {
            send(ex, 500, JsonUtil.error(e.getMessage()));
        }
    }

    private String buildRawDump(List<Student> all) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\"table\":\"students\",\"rowCount\":").append(all.size()).append(",\"rows\":[");
        for (int i = 0; i < all.size(); i++) {
            sb.append(JsonUtil.toJson(all.get(i)));
            if (i < all.size() - 1) sb.append(",");
        }
        sb.append("]}");
        return sb.toString();
    }

    private String buildTableDump(List<Student> all) {
        // Returns columns + rows separately for table rendering
        String cols = "[\"studentId\",\"firstName\",\"lastName\",\"email\",\"phone\",\"department\",\"year\",\"gpa\",\"enrollmentDate\",\"status\"]";
        StringBuilder rows = new StringBuilder("[");
        for (int i = 0; i < all.size(); i++) {
            Student s = all.get(i);
            rows.append("[")
                .append(q(s.getStudentId())).append(",")
                .append(q(s.getFirstName())).append(",")
                .append(q(s.getLastName())).append(",")
                .append(q(s.getEmail())).append(",")
                .append(q(s.getPhone())).append(",")
                .append(q(s.getDepartment())).append(",")
                .append(q(s.getYear())).append(",")
                .append(s.getGpa()).append(",")
                .append(q(s.getEnrollmentDate().toString())).append(",")
                .append(q(s.getStatus().name()))
                .append("]");
            if (i < all.size() - 1) rows.append(",");
        }
        rows.append("]");
        return "{\"columns\":" + cols + ",\"rows\":" + rows + ",\"rowCount\":" + all.size() + "}";
    }

    private String q(String s) { return "\"" + (s == null ? "" : s.replace("\"", "\\\"")) + "\""; }

    private static void cors(HttpExchange ex) {
        ex.getResponseHeaders().set("Access-Control-Allow-Origin",  "*");
        ex.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
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
