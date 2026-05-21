package com.sliit.registration.service;

import com.sliit.registration.model.CourseModule;
import com.sliit.registration.model.EnrollmentRequest;
import com.sliit.registration.model.WaitlistEntry;
import com.sliit.registration.repository.CourseModuleRepository;
import com.sliit.registration.repository.EnrollmentRequestRepository;
import com.sliit.registration.repository.WaitlistRepository;
import com.sliit.registration.service.interfaces.IModeratorService;
import com.sliit.registration.util.SortingUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ModeratorServiceImpl implements IModeratorService {

    private final WaitlistRepository waitlistRepository;
    private final EnrollmentRequestRepository requestRepository;
    private final CourseModuleRepository moduleRepository;

    public ModeratorServiceImpl(WaitlistRepository waitlistRepository,
                                 EnrollmentRequestRepository requestRepository,
                                 CourseModuleRepository moduleRepository) {
        this.waitlistRepository = waitlistRepository;
        this.requestRepository = requestRepository;
        this.moduleRepository = moduleRepository;
    }

    @Override
    public WaitlistEntry addToWaitlist(String studentId, String moduleCode) {
        int nextPos = waitlistRepository.getNextPosition(moduleCode);
        String id = "WL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        WaitlistEntry entry = new WaitlistEntry(id, studentId, moduleCode, nextPos);
        waitlistRepository.save(entry);
        return entry;
    }

    @Override
    public List<WaitlistEntry> getAllWaitlistEntries() {
        return waitlistRepository.findAll();
    }

    @Override
    public List<WaitlistEntry> getWaitlistForModule(String moduleCode) {
        return waitlistRepository.findByModuleCode(moduleCode);
    }

    @Override
    public List<CourseModule> getAllModules() {
        return moduleRepository.findAll();
    }

    @Override
    public boolean updateWaitlistPosition(String waitlistId, int newPosition) {
        Optional<WaitlistEntry> opt = waitlistRepository.findById(waitlistId);
        if (opt.isPresent()) {
            opt.get().setQueuePosition(newPosition);
            waitlistRepository.save(opt.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeFromWaitlist(String waitlistId) {
        Optional<WaitlistEntry> opt = waitlistRepository.findById(waitlistId);
        if (opt.isPresent()) {
            String moduleCode = opt.get().getModuleCode();
            waitlistRepository.deleteById(waitlistId);
            reindexWaitlist(moduleCode);
            return true;
        }
        return false;
    }

    @Override
    public boolean approveFromWaitlist(String waitlistId) {
        log.info("approveFromWaitlist() — waitlistId: '{}'", waitlistId);
        Optional<WaitlistEntry> opt = waitlistRepository.findById(waitlistId);
        if (opt.isPresent()) {
            WaitlistEntry entry = opt.get();
            Optional<CourseModule> modOpt = moduleRepository.findById(entry.getModuleCode());
            if (modOpt.isPresent()) {
                CourseModule mod = modOpt.get();
                if (mod.getCurrentEnrollment() < mod.getMaxCapacity()) {
                    // Update the existing WAITLISTED request if it exists
                    List<EnrollmentRequest> studentRequests = requestRepository.findByStudentId(entry.getStudentId());
                    boolean updated = false;
                    for (EnrollmentRequest req : studentRequests) {
                        if (req.getModuleCode().equals(entry.getModuleCode()) && "WAITLISTED".equals(req.getStatus())) {
                            req.setStatus("APPROVED");
                            requestRepository.save(req);
                            updated = true;
                            break;
                        }
                    }
                    
                    // Fallback: If no waitlisted request found, create one (should not happen if system is consistent)
                    if (!updated) {
                        String reqId = "REQ-WL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
                        EnrollmentRequest req = new EnrollmentRequest(reqId, entry.getStudentId(), entry.getModuleCode(), "APPROVED", System.currentTimeMillis());
                        requestRepository.save(req);
                    }
                    
                    // Update module capacity
                    mod.setCurrentEnrollment(mod.getCurrentEnrollment() + 1);
                    moduleRepository.save(mod);
                    
                    // Remove from waitlist and reindex
                    waitlistRepository.deleteById(waitlistId);
                    reindexWaitlist(entry.getModuleCode());
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String getModuleIdByWaitlistId(String waitlistId) {
        return waitlistRepository.findById(waitlistId)
                .map(WaitlistEntry::getModuleCode)
                .orElse(null);
    }

    private void reindexWaitlist(String moduleCode) {
        List<WaitlistEntry> remaining = waitlistRepository.findByModuleCode(moduleCode);
        for (int i = 0; i < remaining.size(); i++) {
            remaining.get(i).setQueuePosition(i + 1);
            waitlistRepository.save(remaining.get(i));
        }
    }

    // --- AUDIT OPERATIONS ---
    @Override
    public List<EnrollmentRequest> generateBatchList() {
        List<EnrollmentRequest> pending = requestRepository.findByStatus("PENDING");
        SortingUtils.insertionSortRequests(pending);
        return pending;
    }

    @Override
    public List<EnrollmentRequest> getAllRequestsSorted() {
        List<EnrollmentRequest> all = requestRepository.findAll();
        SortingUtils.insertionSortRequests(all);
        return all;
    }

    @Override
    public boolean approveRequest(String requestId) {
        Optional<EnrollmentRequest> opt = requestRepository.findById(requestId);
        if (opt.isPresent()) {
            EnrollmentRequest req = opt.get();
            Optional<CourseModule> moduleOpt = moduleRepository.findById(req.getModuleCode());
            if (moduleOpt.isPresent()) {
                CourseModule mod = moduleOpt.get();
                if (mod.getCurrentEnrollment() < mod.getMaxCapacity()) {
                    req.setStatus("APPROVED");
                    requestRepository.save(req);
                    mod.setCurrentEnrollment(mod.getCurrentEnrollment() + 1);
                    moduleRepository.save(mod);
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean rejectRequest(String requestId) {
        Optional<EnrollmentRequest> opt = requestRepository.findById(requestId);
        if (opt.isPresent()) {
            opt.get().setStatus("REJECTED");
            requestRepository.save(opt.get());
            return true;
        }
        return false;
    }

    @Override
    public void clearProcessedRequests() {
        requestRepository.deleteByStatus("APPROVED");
        requestRepository.deleteByStatus("REJECTED");
    }
}
