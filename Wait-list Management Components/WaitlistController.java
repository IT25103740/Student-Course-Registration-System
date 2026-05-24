package com.sliit.registration.controller;

import com.sliit.registration.model.WaitlistEntry;
import com.sliit.registration.service.interfaces.IAdminService;
import com.sliit.registration.service.interfaces.IModeratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * WaitlistController - Synchronized with Elite Premium Waitlist UI.
 */
@Slf4j
@Controller
@RequestMapping("/moderator/waitlist")
public class WaitlistController {

    private final IModeratorService moderatorService;
    private final IAdminService adminService;

    public WaitlistController(IModeratorService moderatorService, IAdminService adminService) {
        this.moderatorService = moderatorService;
        this.adminService = adminService;
    }

    @GetMapping
    public String waitlistPage(@RequestParam(required = false) String moduleCode, Model model) {
        model.addAttribute("modules", adminService.getAllModules());
        
        if (moduleCode != null && !moduleCode.isEmpty()) {
            model.addAttribute("entries", moderatorService.getWaitlistForModule(moduleCode));
            model.addAttribute("currentModuleId", moduleCode);
        }
        
        return "moderator-waitlist";
    }

    @PostMapping("/approve")
    public String approveFromWaitlist(@RequestParam String waitlistId, RedirectAttributes ra) {
        String moduleId = moderatorService.getModuleIdByWaitlistId(waitlistId);
        boolean success = moderatorService.approveFromWaitlist(waitlistId);
        
        ra.addFlashAttribute(success ? "success" : "error", 
                success ? "Priority entry has been successfully approved." : "Approval failed. Module capacity limit reached.");
        
        return "redirect:/moderator/waitlist" + (moduleId != null ? "?moduleCode=" + moduleId : "");
    }

    @PostMapping("/delete")
    public String removeFromWaitlist(@RequestParam String waitlistId, RedirectAttributes ra) {
        String moduleId = moderatorService.getModuleIdByWaitlistId(waitlistId);
        boolean success = moderatorService.removeFromWaitlist(waitlistId);
        ra.addFlashAttribute(success ? "success" : "error", 
                success ? "Student removed from priority queue." : "Record removal failed.");
        
        return "redirect:/moderator/waitlist" + (moduleId != null ? "?moduleCode=" + moduleId : "");
    }
}
