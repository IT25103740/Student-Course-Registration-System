package com.university;

import com.university.auth.AuthService;
import com.university.controller.StudentController;
import com.university.repository.JsonDatabase;
import com.university.repository.StudentRepository;
import com.university.service.StudentService;

/**
 * Application entry point.
 *
 * Dependency chain (OOP — Dependency Injection):
 *   JsonDatabase (persistence)
 *     → StudentService (business logic)
 *       → StudentController (HTTP API)
 *   AuthService (login/session)
 *
 * Database file: university.db.json  (created next to run.sh)
 * Default logins:
 *   admin  / admin123   (full access)
 *   staff  / staff123   (read + write)
 *   viewer / viewer123  (read only)
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════════════╗");
        System.out.println("║        UniPortal — Student Portal v2.0       ║");
        System.out.println("╚══════════════════════════════════════════════╝");

        // 1. Data layer — file-based JSON database
        StudentRepository repository = new JsonDatabase();

        // 2. Business logic layer
        StudentService service = new StudentService(repository);

        // 3. Auth layer
        AuthService authService = new AuthService();

        // 4. HTTP API layer
        StudentController controller = new StudentController(service, authService);

        try {
            controller.start();
            System.out.println("\n🌐  Open your browser → http://localhost:8080/login.html");
            System.out.println("🔑  Login: admin / admin123");
            System.out.println("📂  Database file: university.db.json  (in this folder)");
            System.out.println("⛔  Press Ctrl+C to stop\n");

            // Graceful shutdown
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("\nShutting down gracefully…");
                controller.stop();
            }));

        } catch (Exception e) {
            System.err.println("❌  Failed to start server: " + e.getMessage());
            System.exit(1);
        }
    }
}
