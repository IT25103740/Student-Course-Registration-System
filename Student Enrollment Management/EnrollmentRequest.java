package com.sliit.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest implements FilePersistable {
    private String requestId;
    private String studentId;
    private String moduleCode;
    private String status;          // PENDING, APPROVED, REJECTED
    private long timestamp;

    @Override
    public String toFileString() {
        return requestId + "|" + studentId + "|" + moduleCode + "|" + status + "|" + timestamp;
    }

    @Override
    public void fromFileString(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 5) {
                this.requestId = parts[0];
                this.studentId = parts[1];
                this.moduleCode = parts[2];
                this.status = parts[3];
                this.timestamp = Long.parseLong(parts[4].trim());
            }
        } catch (Exception e) {
            log.error("Safe Parsing Failed for EnrollmentRequest: {}", e.getMessage());
        }
    }
}
