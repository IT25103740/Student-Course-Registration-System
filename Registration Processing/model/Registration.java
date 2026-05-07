package com.group.registration.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Registration {
    private String requestId;
    private String studentId;
    private List<String> selectedModules = new ArrayList<>();
    private String status = "PENDING";
    private LocalDateTime submissionDate;
    private LocalDateTime lastUpdated;

    public Registration() {
        this.submissionDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
    }

    // Getters and Setters
    public String getRequestId() { return requestId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public List<String> getSelectedModules() { return selectedModules; }
    public void setSelectedModules(List<String> selectedModules) { this.selectedModules = selectedModules; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public LocalDateTime getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDateTime submissionDate) { this.submissionDate = submissionDate; }

    public LocalDateTime getLastUpdated() { return lastUpdated; }
    public void setLastUpdated(LocalDateTime lastUpdated) { this.lastUpdated = lastUpdated; }

    @Override
    public String toString() {
        return requestId + "|" + studentId + "|" +
                String.join(",", selectedModules) + "|" +
                status + "|" + submissionDate + "|" + lastUpdated;
    }
}