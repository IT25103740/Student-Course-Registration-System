package com.sliit.registration.service;

import com.sliit.registration.dto.CourseModuleDto;
import com.sliit.registration.factory.UserFactory;
import com.sliit.registration.model.*;
import com.sliit.registration.repository.CourseModuleRepository;
import com.sliit.registration.repository.StudentProfileRepository;
import com.sliit.registration.repository.UserRepository;
import com.sliit.registration.repository.AuthenticationLogRepository;
import com.sliit.registration.service.interfaces.IAdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminServiceImpl implements IAdminService {

    private final CourseModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final AuthenticationLogRepository logRepository;

    public AdminServiceImpl(CourseModuleRepository moduleRepository, 
                            UserRepository userRepository,
                            StudentProfileRepository studentProfileRepository,
                            AuthenticationLogRepository logRepository) {
        this.moduleRepository = moduleRepository;
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.logRepository = logRepository;
    }

    @Override
    public CourseModule addModule(CourseModuleDto dto) {
        log.info("Attempting to provision module: {}", dto.getModuleId());
        
        // Safety Check: Avoid duplicate IDs
        if (moduleRepository.findById(dto.getModuleId()).isPresent()) {
            log.warn("Provisioning blocked: Module {} already exists.", dto.getModuleId());
            throw new IllegalArgumentException("Module ID " + dto.getModuleId() + " is already registered.");
        }

        CourseModule module = new CourseModule(dto.getModuleId().trim(), dto.getModuleName().trim(), dto.getMaxCapacity(), 0);
        moduleRepository.save(module);
        return module;
    }

    @Override
    public List<CourseModule> getAllModules() {
        return moduleRepository.findAll();
    }

    @Override
    public boolean updateModuleCapacity(String moduleId, int newCapacity) {
        Optional<CourseModule> opt = moduleRepository.findById(moduleId);
        if (opt.isPresent()) {
            opt.get().setMaxCapacity(newCapacity);
            moduleRepository.save(opt.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean removeModule(String moduleId) {
        moduleRepository.deleteById(moduleId);
        return true;
    }

    @Override
    public User registerUser(String userId, String username, String password, String role) {
        log.info("Processing registration for User ID: {}", userId);
        
        // 1. Uniqueness Check
        if (userRepository.findById(userId).isPresent()) {
            log.warn("Registration failed: User ID {} already exists.", userId);
            throw new com.sliit.registration.exception.UserAlreadyExistsException(userId);
        }

        // 2. Factory Creation
        User user = UserFactory.createUser(userId, username, password, role, null);
        if (user == null) {
            log.error("Factory failed to create user for role: {}", role);
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        // 3. Save User
        userRepository.save(user);
        log.info("User {} successfully saved to repository.", userId);

        // 4. Auto-Profile for Students
        if (user instanceof Student) {
            StudentProfile profile = new StudentProfile(userId, username, "student@eduall.com", "N/A", "General", true);
            studentProfileRepository.save(profile);
            log.info("Student profile created for: {}", userId);
        }

        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean updateUserPassword(String userId, String newPassword) {
        Optional<User> opt = userRepository.findById(userId);
        if (opt.isPresent()) {
            User user = opt.get();
            user.setPasswordHash(newPassword);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(String userId) {
        userRepository.delete(userId);
        return true;
    }

    @Override
    public List<AuthenticationLog> getAuthenticationLogs() {
        return logRepository.findAll();
    }
}
