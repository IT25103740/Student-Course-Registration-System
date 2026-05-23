package com.sliit.registration.controller;

import com.sliit.registration.dto.CourseModuleDto;
import com.sliit.registration.model.CourseModule;
import com.sliit.registration.service.interfaces.IAdminService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * ModuleController - Hardened with Nuclear Anti-Cache headers for Firefox compatibility.
 */
@Slf4j
@Controller
@RequestMapping("/admin/modules")
public class ModuleController {

    private final IAdminService adminService;

    public ModuleController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String modulesPage(Model model, HttpServletResponse response) {
        // --- NUCLEAR ANTI-CACHE HEADERS ---
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");

        List<CourseModule> modules = adminService.getAllModules();
        
        // Add a "Server Pulse" timestamp to prove the UI is fresh
        String pulse = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        
        model.addAttribute("modules", modules);
        model.addAttribute("serverPulse", pulse);
        
        log.info("Dashboard Rendered at {} | Modules in Sync: {}", pulse, modules.size());
        return "admin-curriculum";
    }

    @PostMapping("/add")
    public String addModule(@RequestParam String moduleId, 
                            @RequestParam String moduleName,
                            @RequestParam int maxCapacity,
                            RedirectAttributes ra) {
        
        try {
            CourseModuleDto dto = new CourseModuleDto(moduleId.trim(), moduleName.trim(), maxCapacity);
            adminService.addModule(dto);
            ra.addFlashAttribute("success", "Module [" + moduleId + "] registered successfully!");
        } catch (IllegalArgumentException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            ra.addFlashAttribute("error", "System Error: " + e.getMessage());
        }
        
        // Force refresh via cache-buster query param
        return "redirect:/admin/modules?v=" + System.currentTimeMillis();
    }

    @PostMapping("/update")
    public String updateCapacity(@RequestParam String moduleId, @RequestParam int newCapacity,
                                  RedirectAttributes ra) {
        boolean updated = adminService.updateModuleCapacity(moduleId, newCapacity);
        ra.addFlashAttribute(updated ? "success" : "error",
                updated ? "Capacity updated for " + moduleId : "Module not found.");
        return "redirect:/admin/modules?v=" + System.currentTimeMillis();
    }

    @PostMapping("/delete")
    public String deleteModule(@RequestParam String moduleId, RedirectAttributes ra) {
        boolean deleted = adminService.removeModule(moduleId);
        ra.addFlashAttribute(deleted ? "success" : "error",
                deleted ? "Module removed." : "Error.");
        return "redirect:/admin/modules?v=" + System.currentTimeMillis();
    }
}
