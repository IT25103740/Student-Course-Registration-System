package com.sliit.registration.controller;

import com.sliit.registration.model.User;
import com.sliit.registration.service.interfaces.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * UserManagementController - Professional implementation with robust error feedback.
 */
@Slf4j
@Controller
@RequestMapping("/admin/users")
public class UserManagementController {

    private final IAdminService adminService;

    public UserManagementController(IAdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping
    public String usersPage(Model model) {
        List<User> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("logs", adminService.getAuthenticationLogs());
        return "admin-users";
    }

    @PostMapping("/add")
    public String addUser(@RequestParam(required = false) String userId, 
                          @RequestParam(required = false) String username,
                          @RequestParam(required = false) String password, 
                          @RequestParam(required = false) String role, 
                          RedirectAttributes ra) {
        
        log.info("Received request to add user: ID={}, Name={}, Role={}", userId, username, role);

        if (userId == null || userId.isBlank() || username == null || username.isBlank() || role == null) {
            ra.addFlashAttribute("error", "All fields are required for registration.");
            return "redirect:/admin/users";
        }

        try {
            adminService.registerUser(userId.trim(), username.trim(), password, role.trim().toUpperCase());
            ra.addFlashAttribute("success", "User [" + username + "] registered successfully!");
            log.info("Successfully registered user: {}", userId);
        } catch (com.sliit.registration.exception.UserAlreadyExistsException e) {
            ra.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            log.error("Registration failed for ID {}: {}", userId, e.getMessage());
            ra.addFlashAttribute("error", "Registration error: " + e.getMessage());
        }

        return "redirect:/admin/users";
    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String userId, @RequestParam String newPassword,
                                  RedirectAttributes ra) {
        boolean updated = adminService.updateUserPassword(userId.trim(), newPassword);
        ra.addFlashAttribute(updated ? "success" : "error",
                updated ? "Password updated successfully!" : "User search failed.");
        return "redirect:/admin/users";
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam String userId, RedirectAttributes ra) {
        boolean deleted = adminService.deleteUser(userId.trim());
        ra.addFlashAttribute(deleted ? "success" : "error",
                deleted ? "User access has been revoked." : "Operation could not be completed.");
        return "redirect:/admin/users";
    }
}
