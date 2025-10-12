package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Admin;
import com.glebzapara.test_project.models.Student;
import com.glebzapara.test_project.repositories.AdminRepository;
import com.glebzapara.test_project.util.PhoneNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
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

    public String getAdminCountryById(Integer id) throws Exception {
        return adminRepository.findById(id)
                .map(Admin::getCountry)
                .orElseThrow(() -> new Exception("Country cannot be null"));
    }

    public String getAdminPhoneNumberById(Integer id) throws Exception {
        return adminRepository.findById(id)
                .map(Admin::getPhoneNumber)
                .orElseThrow(() -> new Exception("Phone number cannot be null"));
    }

    public byte[] getAdminImageById(Integer id) throws IOException {
        String imagePath = adminRepository.findById(id)
                .map(Admin::getImage)
                .orElse(null);

        if (imagePath == null || imagePath.isEmpty()) return null;

        Path path = Paths.get("G:/IdeaProjects/test_project/src/main/resources/static", imagePath);
        if (!Files.exists(path)) return null;

        return Files.readAllBytes(path);
    }

    public void saveAdminImage(Integer id, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return;
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            System.out.println("Uploaded file is not an image, skipping.");
            return;
        }

        Path uploadDir = Paths.get("G:/IdeaProjects/test_project/src/main/resources/static/images");
        Files.createDirectories(uploadDir);

        String originalFileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String extension = originalFileName.contains(".")
                ? originalFileName.substring(originalFileName.lastIndexOf("."))
                : "";
        String fileName = id + "_" + UUID.randomUUID() + extension;

        Path target = uploadDir.resolve(fileName);
        file.transferTo(target);

        adminRepository.findById(id).ifPresent(s -> {
            s.setImage("images/" + fileName);
            adminRepository.save(s);
        });
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
        if (admin.getPhoneNumber() == null || admin.getPhoneNumber().trim().isEmpty()) {
            throw new Exception("Phone number cannot be null or empty");
        }

        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setPhoneNumber(PhoneNumberUtil.addCountryCode(admin.getCountry(), admin.getPhoneNumber()));

        adminRepository.save(admin);
    }
}
