package com.sliit.registration.controller;

import com.sliit.registration.model.EnrollmentRequest;
import com.sliit.registration.service.interfaces.IModeratorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RequestAuditController - Synchronized with Elite Premium Auditing UI.
 */
@Controller
@RequestMapping("/moderator/audit")
public class RequestAuditController {

    private final IModeratorService moderatorService;

    public RequestAuditController(IModeratorService moderatorService) {
        this.moderatorService = moderatorService;
    }

    @GetMapping
    public String auditPage(Model model) {
        List<EnrollmentRequest> all = moderatorService.getAllRequestsSorted();
        
        // Filter into Pending (for Batch) and All (for History)
        List<EnrollmentRequest> pending = all.stream()
                .filter(r -> "PENDING".equalsIgnoreCase(r.getStatus()))
                .collect(Collectors.toList());
                
        model.addAttribute("requests", all);
        model.addAttribute("batchList", pending);
        
        return "moderator-auditing";
    }

    @PostMapping("/approve")
    public String approveRequest(@RequestParam(required = false) List<String> requestIds, 
                                 @RequestParam(required = false) String requestId, 
                                 RedirectAttributes ra) {
        
        if (requestIds != null && !requestIds.isEmpty()) {
            // Batch Process
            int count = 0;
            for (String id : requestIds) {
                if (moderatorService.approveRequest(id)) count++;
            }
            ra.addFlashAttribute("success", "Batch Resolution: " + count + " requests approved.");
        } else if (requestId != null) {
            // Single Process
            boolean approved = moderatorService.approveRequest(requestId);
            ra.addFlashAttribute(approved ? "success" : "error",
                    approved ? "Request [" + requestId + "] approved!" : "Approval failed (Module capacity reached).");
        }
        
        return "redirect:/moderator/audit";
    }

    @PostMapping("/reject")
    public String rejectRequest(@RequestParam(required = false) List<String> requestIds, 
                                 @RequestParam(required = false) String requestId, 
                                 RedirectAttributes ra) {
        
        if (requestIds != null && !requestIds.isEmpty()) {
            // Batch Process
            int count = 0;
            for (String id : requestIds) {
                if (moderatorService.rejectRequest(id)) count++;
            }
            ra.addFlashAttribute("success", "Batch Resolution: " + count + " requests rejected.");
        } else if (requestId != null) {
            // Single Process
            boolean rejected = moderatorService.rejectRequest(requestId);
            ra.addFlashAttribute(rejected ? "success" : "error",
                    rejected ? "Request [" + requestId + "] rejected." : "Request not found.");
        }
        
        return "redirect:/moderator/audit";
    }

    @PostMapping("/clear")
    public String clearProcessed(RedirectAttributes ra) {
        moderatorService.clearProcessedRequests();
        ra.addFlashAttribute("success", "Audit log archives have been cleared.");
        return "redirect:/moderator/audit";
    }
}
