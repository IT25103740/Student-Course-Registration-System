package com.university.controller;

import com.university.model.Student;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Lightweight JSON serialiser — no external library needed.
 * Demonstrates static utility methods and string building in Java.
 */
public final class JsonUtil {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

    private JsonUtil() {} // utility class — no instances

    // ── Student → JSON ────────────────────────────────────────────────────────

    public static String toJson(Student s) {
        return "{"
            + field("studentId",       s.getStudentId())       + ","
            + field("firstName",       s.getFirstName())       + ","
            + field("lastName",        s.getLastName())        + ","
            + field("fullName",        s.getFullName())        + ","
            + field("email",           s.getEmail())           + ","
            + field("phone",           s.getPhone())           + ","
            + field("department",      s.getDepartment())      + ","
            + field("year",            s.getYear())            + ","
            + numField("gpa",          s.getGpa())             + ","
            + field("enrollmentDate",  s.getEnrollmentDate().format(DATE_FMT)) + ","
            + field("status",          s.getStatus().name())   + ","
            + arrayField("courses",    s.getEnrolledCourses())
            + "}";
    }

    public static String toJsonArray(List<Student> students) {
        return "[" + students.stream().map(JsonUtil::toJson).collect(Collectors.joining(",")) + "]";
    }

    // ── Map → JSON ────────────────────────────────────────────────────────────

    public static String toJson(Map<String, Object> map) {
        return "{" + map.entrySet().stream()
            .map(e -> "\"" + e.getKey() + "\":" + jsonValue(e.getValue()))
            .collect(Collectors.joining(",")) + "}";
    }

    // ── Error / success wrappers ──────────────────────────────────────────────

    public static String error(String message) {
        return "{\"error\":true,\"message\":" + quote(message) + "}";
    }

    public static String success(String message) {
        return "{\"success\":true,\"message\":" + quote(message) + "}";
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static String field(String key, String value) {
        return "\"" + key + "\":" + quote(value);
    }

    private static String numField(String key, double value) {
        return "\"" + key + "\":" + String.format("%.2f", value);
    }

    private static String arrayField(String key, List<String> list) {
        String arr = "[" + list.stream().map(JsonUtil::quote).collect(Collectors.joining(",")) + "]";
        return "\"" + key + "\":" + arr;
    }

    private static String quote(String s) {
        if (s == null) return "null";
        return "\"" + s.replace("\\", "\\\\").replace("\"", "\\\"") + "\"";
    }

    private static String jsonValue(Object v) {
        if (v == null) return "null";
        if (v instanceof Number) return v.toString();
        if (v instanceof Boolean) return v.toString();
        return quote(v.toString());
    }
}
