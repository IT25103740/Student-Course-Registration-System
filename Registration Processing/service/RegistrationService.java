package com.group.registration.service;

import com.group.registration.model.Registration;
import com.group.registration.util.FileUtil;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RegistrationService {

    public String submitRegistration(String studentId, List<String> modules) {
        Registration reg = new Registration();
        reg.setRequestId("REG-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        reg.setStudentId(studentId);
        reg.setSelectedModules(modules);

        FileUtil.appendLine(reg.toString());
        return reg.getRequestId();
    }

    public List<Registration> getRegistrationsByStudent(String studentId) {
        List<Registration> list = new ArrayList<>();
        for (String line : FileUtil.readAllLines()) {
            String[] parts = line.split("\\|", -1);
            if (parts.length >= 4 && parts[1].equals(studentId)) {
                list.add(parseRegistration(parts));
            }
        }
        return list;
    }

    public boolean updateRegistration(String requestId, List<String> newModules) {
        List<String> lines = FileUtil.readAllLines();
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            if (parts[0].equals(requestId) && "PENDING".equals(parts[3])) {
                parts[2] = String.join(",", newModules);
                parts[5] = LocalDateTime.now().toString();
                lines.set(i, String.join("|", parts));
                FileUtil.writeAllLines(lines);
                return true;
            }
        }
        return false;
    }

    public boolean cancelRegistration(String requestId) {
        List<String> lines = FileUtil.readAllLines();
        for (int i = 0; i < lines.size(); i++) {
            String[] parts = lines.get(i).split("\\|", -1);
            if (parts[0].equals(requestId) && "PENDING".equals(parts[3])) {
                parts[3] = "WITHDRAWN";
                parts[5] = LocalDateTime.now().toString();
                lines.set(i, String.join("|", parts));
                FileUtil.writeAllLines(lines);
                return true;
            }
        }
        return false;
    }

    private Registration parseRegistration(String[] parts) {
        Registration reg = new Registration();
        reg.setRequestId(parts[0]);
        reg.setStudentId(parts[1]);
        if (parts.length > 2 && !parts[2].isEmpty()) {
            for (String m : parts[2].split(",")) {
                if (!m.trim().isEmpty()) reg.getSelectedModules().add(m.trim());
            }
        }
        reg.setStatus(parts[3]);
        return reg;
    }
}