package com.sliit.registration.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseModule implements FilePersistable {
    private String moduleId;
    private String moduleName;
    private int maxCapacity;
    private int currentEnrollment;

    @Override
    public String toFileString() {
        return moduleId + "|" + moduleName + "|" + maxCapacity + "|" + currentEnrollment;
    }

    @Override
    public void fromFileString(String line) {
        try {
            String[] parts = line.split("\\|");
            if (parts.length >= 4) {
                this.moduleId = parts[0].trim();
                this.moduleName = parts[1].trim();
                this.maxCapacity = Integer.parseInt(parts[2].trim());
                this.currentEnrollment = Integer.parseInt(parts[3].trim());
            }
        } catch (Exception e) {
            log.error("Safe Parsing Failed for CourseModule: {}", e.getMessage());
        }
    }
}
