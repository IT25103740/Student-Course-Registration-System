package com.university.repository;

import com.university.model.Student;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * JsonDatabase — file-based persistent repository.
 *
 * Reads and writes a human-readable JSON file: university.db.json
 * Located in the current working directory (next to run.sh / run.bat).
 *
 * You can open university.db.json in:
 *   - Any text editor (Notepad, VS Code, IntelliJ)
 *   - Online JSON viewers (jsonviewer.stack.hu, jsonformatter.curiousconcept.com)
 *   - DB Browser for SQLite alternative: JSON Viewer extension in VS Code
 *
 * OOP: Implements StudentRepository (polymorphism), file I/O, thread-safety.
 */
public class JsonDatabase implements StudentRepository {

    private static final Logger LOG = Logger.getLogger(JsonDatabase.class.getName());
    private static final String DB_FILE = "university.db.json";

    private final Map<String, Student> store      = new ConcurrentHashMap<>();
    private final Map<String, String>  emailIndex = new ConcurrentHashMap<>();
    private final Path                 dbPath;

    // ── Constructor ──────────────────────────────────────────────────────────
    public JsonDatabase() {
        this.dbPath = Paths.get(DB_FILE);
        load();
        if (store.isEmpty()) {
            seedData();
            save();
        }
        LOG.info("📂 Database file: " + dbPath.toAbsolutePath());
    }

    // ── Seed data ────────────────────────────────────────────────────────────
    private void seedData() {
        List<Student> seeds = List.of(
            Student.create("Amal",    "Perera",      "amal.perera@uni.lk",    "+94771234567", "Computer Science", "2nd", 3.75),
            Student.create("Nimal",   "Silva",       "nimal.silva@uni.lk",    "+94772345678", "Engineering",      "3rd", 3.20),
            Student.create("Kamali",  "Fernando",    "kamali.f@uni.lk",       "+94773456789", "Business",         "1st", 3.90),
            Student.create("Ruwan",   "Jayawardena", "ruwan.j@uni.lk",        "+94774567890", "Medicine",         "4th", 3.55),
            Student.create("Dilani",  "Wijesinghe",  "dilani.w@uni.lk",       "+94775678901", "Computer Science", "1st", 3.10),
            Student.create("Kasun",   "Bandara",     "kasun.b@uni.lk",        "+94776789012", "Engineering",      "2nd", 3.60),
            Student.create("Thilini", "Rajapaksa",   "thilini.r@uni.lk",      "+94777890123", "Law",              "3rd", 3.80),
            Student.create("Priya",   "Gunawardena", "priya.g@uni.lk",        "+94778901234", "Arts",             "2nd", 3.40)
        );
        seeds.forEach(s -> {
            store.put(s.getStudentId(), s);
            emailIndex.put(s.getEmail(), s.getStudentId());
        });
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────
    @Override
    public Student save(Student student) {
        if (emailIndex.containsKey(student.getEmail())) {
            throw new com.university.exception.DuplicateStudentException(
                "A student with email '" + student.getEmail() + "' already exists.");
        }
        store.put(student.getStudentId(), student);
        emailIndex.put(student.getEmail(), student.getStudentId());
        save();
        return student;
    }

    @Override
    public Optional<Student> findById(String id) { return Optional.ofNullable(store.get(id)); }

    @Override
    public Optional<Student> findByEmail(String email) {
        String id = emailIndex.get(email.toLowerCase());
        return id == null ? Optional.empty() : findById(id);
    }

    @Override
    public List<Student> findAll() { return new ArrayList<>(store.values()); }

    @Override
    public List<Student> searchByName(String query) {
        String q = query.toLowerCase();
        return store.values().stream()
            .filter(s -> s.getFullName().toLowerCase().contains(q))
            .collect(Collectors.toList());
    }

    @Override
    public List<Student> findByDepartment(String dept) {
        return store.values().stream()
            .filter(s -> s.getDepartment().equalsIgnoreCase(dept))
            .collect(Collectors.toList());
    }

    @Override
    public Student update(Student student) {
        Student existing = store.get(student.getStudentId());
        if (existing == null)
            throw new com.university.exception.StudentNotFoundException("Not found: " + student.getStudentId());
        if (!existing.getEmail().equals(student.getEmail())) {
            if (emailIndex.containsKey(student.getEmail()))
                throw new com.university.exception.DuplicateStudentException("Email already in use.");
            emailIndex.remove(existing.getEmail());
            emailIndex.put(student.getEmail(), student.getStudentId());
        }
        store.put(student.getStudentId(), student);
        save();
        return student;
    }

    @Override
    public boolean deleteById(String id) {
        Student removed = store.remove(id);
        if (removed != null) { emailIndex.remove(removed.getEmail()); save(); return true; }
        return false;
    }

    @Override
    public long count() { return store.size(); }

    // ── Persistence ──────────────────────────────────────────────────────────

    /** Write all students to university.db.json */
    private void save() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("{\n  \"_info\": \"UniPortal Student Database — open this file in any text editor or JSON viewer\",\n");
            sb.append("  \"_lastUpdated\": \"").append(java.time.LocalDateTime.now()).append("\",\n");
            sb.append("  \"_totalStudents\": ").append(store.size()).append(",\n");
            sb.append("  \"students\": [\n");
            List<Student> all = new ArrayList<>(store.values());
            for (int i = 0; i < all.size(); i++) {
                sb.append(toJson(all.get(i)));
                if (i < all.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("  ]\n}\n");
            Files.writeString(dbPath, sb.toString(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOG.warning("Could not save database: " + e.getMessage());
        }
    }

    /** Read students from university.db.json on startup */
    private void load() {
        if (!Files.exists(dbPath)) return;
        try {
            String json = Files.readString(dbPath, StandardCharsets.UTF_8);
            parseStudents(json).forEach(s -> {
                store.put(s.getStudentId(), s);
                emailIndex.put(s.getEmail(), s.getStudentId());
            });
            LOG.info("Loaded " + store.size() + " students from " + DB_FILE);
        } catch (Exception e) {
            LOG.warning("Could not load database: " + e.getMessage() + " — starting fresh.");
        }
    }

    // ── Mini JSON parser ──────────────────────────────────────────────────────

    private String toJson(Student s) {
        return String.format(
            "    {\n" +
            "      \"studentId\": \"%s\",\n" +
            "      \"firstName\": \"%s\",\n" +
            "      \"lastName\": \"%s\",\n" +
            "      \"email\": \"%s\",\n" +
            "      \"phone\": \"%s\",\n" +
            "      \"department\": \"%s\",\n" +
            "      \"year\": \"%s\",\n" +
            "      \"gpa\": %.2f,\n" +
            "      \"enrollmentDate\": \"%s\",\n" +
            "      \"status\": \"%s\"\n" +
            "    }",
            esc(s.getStudentId()), esc(s.getFirstName()), esc(s.getLastName()),
            esc(s.getEmail()), esc(s.getPhone()), esc(s.getDepartment()),
            esc(s.getYear()), s.getGpa(),
            s.getEnrollmentDate().toString(), s.getStatus().name()
        );
    }

    private List<Student> parseStudents(String json) {
        List<Student> result = new ArrayList<>();
        // Find the students array
        int arrStart = json.indexOf("\"students\"");
        if (arrStart < 0) return result;
        // Split on student objects
        String[] objects = json.substring(arrStart).split("\\{");
        for (String obj : objects) {
            if (!obj.contains("studentId")) continue;
            try {
                String id     = extractField(obj, "studentId");
                String fn     = extractField(obj, "firstName");
                String ln     = extractField(obj, "lastName");
                String email  = extractField(obj, "email");
                String phone  = extractField(obj, "phone");
                String dept   = extractField(obj, "department");
                String year   = extractField(obj, "year");
                String gpaStr = extractField(obj, "gpa");
                String dateStr= extractField(obj, "enrollmentDate");
                String status = extractField(obj, "status");

                double gpa  = Double.parseDouble(gpaStr.trim());
                LocalDate d = LocalDate.parse(dateStr.trim());
                Student.StudentStatus st = Student.StudentStatus.valueOf(status.trim());

                Student s = new Student(id, fn, ln, email, phone, dept, year, gpa, d, st);
                result.add(s);
            } catch (Exception ignored) {}
        }
        return result;
    }

    private String extractField(String obj, String key) {
        // Matches: "key": "value"  OR  "key": 3.75
        String pattern = "\"" + key + "\"";
        int idx = obj.indexOf(pattern);
        if (idx < 0) return "";
        int colon = obj.indexOf(":", idx);
        if (colon < 0) return "";
        String rest = obj.substring(colon + 1).trim();
        if (rest.startsWith("\"")) {
            int end = rest.indexOf("\"", 1);
            return end < 0 ? "" : rest.substring(1, end);
        } else {
            // number or keyword
            StringBuilder sb = new StringBuilder();
            for (char c : rest.toCharArray()) {
                if (c == ',' || c == '\n' || c == '\r' || c == '}') break;
                sb.append(c);
            }
            return sb.toString().trim();
        }
    }

    private String esc(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
