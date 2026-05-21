package com.sliit.registration.service;

import com.sliit.registration.model.CourseModule;
import com.sliit.registration.model.EnrollmentRequest;
import com.sliit.registration.model.StudentProfile;
import com.sliit.registration.model.WaitlistEntry;
import com.sliit.registration.repository.CourseModuleRepository;
import com.sliit.registration.repository.EnrollmentRequestRepository;
import com.sliit.registration.repository.StudentProfileRepository;
import com.sliit.registration.repository.WaitlistRepository;
import com.sliit.registration.service.interfaces.IStudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * StudentServiceImpl — implements IStudentService.
 */
@Slf4j
@Service
public class StudentServiceImpl implements IStudentService {

    private final EnrollmentRequestRepository requestRepository;
    private final CourseModuleRepository moduleRepository;
    private final StudentProfileRepository profileRepository;
    private final WaitlistRepository waitlistRepository;

    public StudentServiceImpl(EnrollmentRequestRepository requestRepository,
                               CourseModuleRepository moduleRepository,
                               StudentProfileRepository profileRepository,
                               WaitlistRepository waitlistRepository) {
        this.requestRepository = requestRepository;
        this.moduleRepository = moduleRepository;
        this.profileRepository = profileRepository;
        this.waitlistRepository = waitlistRepository;
        log.info("StudentServiceImpl initialized via constructor injection");
    }

    @Override
    public EnrollmentRequest submitEnrollmentRequest(String studentId, String moduleCode) {
        log.info("submitEnrollmentRequest() — student: '{}', module: '{}'", studentId, moduleCode);
        String status = "PENDING";
        Optional<CourseModule> moduleOpt = moduleRepository.findById(moduleCode);
        boolean isFull = false;
        
        if (moduleOpt.isPresent()) {
            CourseModule module = moduleOpt.get();
            // Real-Time Validation: Count both APPROVED and PENDING requests to prevent over-subscription
            long activeRequests = requestRepository.findAll().stream()
                    .filter(r -> r.getModuleCode().equals(moduleCode) && 
                            ("APPROVED".equals(r.getStatus()) || "PENDING".equals(r.getStatus())))
                    .count();

            if (activeRequests >= module.getMaxCapacity()) {
                status = "WAITLISTED";
                isFull = true;
            }
        }
        String requestId = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        EnrollmentRequest request = new EnrollmentRequest(requestId, studentId, moduleCode, status, System.currentTimeMillis());
        requestRepository.save(request);
        if (isFull) {
            String waitlistId = "WLT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            int nextPos = waitlistRepository.getNextPosition(moduleCode);
            WaitlistEntry entry = new WaitlistEntry(waitlistId, studentId, moduleCode, nextPos);
            waitlistRepository.save(entry);
        }
        return request;
    }

    @Override
    public List<EnrollmentRequest> getStudentRequests(String studentId) {
        return requestRepository.findByStudentId(studentId);
    }

    @Override
    public List<CourseModule> getAvailableModules() {
        return moduleRepository.findAll();
    }

    @Override
    public boolean swapModule(String requestId, String newModuleCode) {
        Optional<EnrollmentRequest> opt = requestRepository.findById(requestId);
        if (opt.isPresent() && "PENDING".equals(opt.get().getStatus())) {
            EnrollmentRequest req = opt.get();
            req.setModuleCode(newModuleCode);
            req.setTimestamp(System.currentTimeMillis());
            requestRepository.save(req);
            return true;
        }
        return false;
    }

    @Override
    public boolean withdrawRequest(String requestId) {
        Optional<EnrollmentRequest> opt = requestRepository.findById(requestId);
        if (opt.isPresent() && "PENDING".equals(opt.get().getStatus())) {
            requestRepository.deleteById(requestId);
            return true;
        }
        return false;
    }

    @Override
    public StudentProfile createProfile(String studentId, String fullName, String email, String phone, String program) {
        // Fix: Use findById to match stable repository
        Optional<StudentProfile> existing = profileRepository.findById(studentId);
        if (existing.isPresent()) return existing.get();
        StudentProfile profile = new StudentProfile(studentId, fullName, email, phone, program, true);
        profileRepository.save(profile);
        return profile;
    }

    @Override
    public Optional<StudentProfile> getProfile(String studentId) {
        // Fix: Use findById to match stable repository
        return profileRepository.findById(studentId);
    }

    @Override
    public boolean updateContactInfo(String studentId, String email, String phone) {
        // Fix: Use findById to match stable repository
        Optional<StudentProfile> opt = profileRepository.findById(studentId);
        if (opt.isPresent()) {
            StudentProfile profile = opt.get();
            profile.setEmail(email);
            profile.setPhone(phone);
            profileRepository.save(profile);
            return true;
        }
        return false;
    }

    @Override
    public boolean deactivateProfile(String studentId) {
        // Fix: Use findById to match stable repository
        Optional<StudentProfile> opt = profileRepository.findById(studentId);
        if (opt.isPresent()) {
            StudentProfile profile = opt.get();
            profile.setActive(false);
            profileRepository.save(profile);
            return true;
        }
        return false;
    }
}
