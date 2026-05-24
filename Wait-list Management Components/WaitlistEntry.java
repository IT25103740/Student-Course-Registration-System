package com.sliit.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WaitlistEntry implements FilePersistable {
    private String waitlistId;
    private String studentId;
    private String moduleCode;
    private int queuePosition;      // FIFO position

    @Override
    public String toFileString() {
        return waitlistId + "|" + studentId + "|" + moduleCode + "|" + queuePosition;
    }

    @Override
    public void fromFileString(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                this.waitlistId = parts[0];
                this.studentId = parts[1];
                this.moduleCode = parts[2];
                this.queuePosition = Integer.parseInt(parts[3].trim());
            }
        } catch (Exception e) {
            log.error("Safe Parsing Failed for WaitlistEntry: {}", e.getMessage());
        }
    }
}
