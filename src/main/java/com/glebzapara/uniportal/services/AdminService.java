package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Admin;
import com.glebzapara.uniportal.models.Department;
import com.glebzapara.uniportal.repositories.AdminRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class AdminService {
    AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Admin findById(Integer id) {
        return adminRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    public void deleteById(Integer id) {
        adminRepository.deleteById(id);
    }

    public void registerAdmin(Admin admin) throws Exception {
        if (admin.getName() == null || admin.getName().trim().isEmpty()) {
            throw new Exception("Name cannot be null or empty");
        }

        if (admin.getSurname() == null || admin.getSurname().trim().isEmpty()) {
            throw new Exception("Surname cannot be null or empty");
        }

        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            throw new Exception("Email cannot be null or empty");
        }

        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            throw new Exception("Password cannot be null or empty");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        adminRepository.save(admin);
    }
}
