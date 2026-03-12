package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Admin;
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

    public List<Admin> findAllAdmins() {
        return adminRepository.findAll();
    }

    public Optional<Admin> findOneAdmin(Integer id) {
        return adminRepository.findById(id);
    }

    public String getAdminNameById(Integer id) throws Exception {
        return adminRepository.findById(id)
                .map(Admin::getName)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

    public String getAdminSurnameById(Integer id) throws Exception {
        return adminRepository.findById(id)
                .map(Admin::getSurname)
                .orElseThrow(() -> new Exception("Surname cannot be null"));
    }

    public String getAdminEmailById(Integer id) throws Exception {
        return adminRepository.findById(id)
                .map(Admin::getEmail)
                .orElseThrow(() -> new Exception("Email cannot be null"));
    }

    public String getAdminPasswordById(Integer id) throws Exception {
        return adminRepository.findById(id)
                .map((Admin::getPassword))
                .orElseThrow(() -> new Exception("Password cannot be null"));
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
