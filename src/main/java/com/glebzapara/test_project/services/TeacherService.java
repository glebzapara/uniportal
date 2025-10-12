package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Student;
import com.glebzapara.test_project.models.Teacher;
import com.glebzapara.test_project.repositories.TeacherRepository;
import com.glebzapara.test_project.util.PhoneNumberUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherService {
    TeacherRepository teacherRepository;
    private PasswordEncoder passwordEncoder;

    public TeacherService(TeacherRepository teacherRepository,
                          PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findOneTeacher(Integer id) {
        return teacherRepository.findById(id);
    }

    public String getTeacherNameById(Integer id) throws Exception {
        return teacherRepository.findById(id)
                .map(Teacher::getName)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

    public String getTeacherSurnameById(Integer id) throws Exception {
        return teacherRepository.findById(id)
                .map(Teacher::getSurname)
                .orElseThrow(() -> new Exception("Surname cannot be null"));
    }

    public String getTeacherEmailById(Integer id) throws Exception {
        return teacherRepository.findById(id)
                .map(Teacher::getEmail)
                .orElseThrow(() -> new Exception("Email cannot be null"));
    }

    public String getTeacherCountryById(Integer id) throws Exception {
        return teacherRepository.findById(id)
                .map(Teacher::getCountry)
                .orElseThrow(() -> new Exception("Country cannot be null"));
    }

    public String getTeacherPasswordById(Integer id) throws Exception {
        return teacherRepository.findById(id)
                .map(Teacher::getPassword)
                .orElseThrow(() -> new Exception("Password cannot be null"));
    }

    public String getTeacherPhoneNumberById(Integer id) throws Exception {
        return teacherRepository.findById(id)
                .map(Teacher::getPhoneNumber)
                .orElseThrow(() -> new Exception("Phone number cannot be null"));
    }

    public byte[] getTeacherImageById(Integer id) throws IOException {
        String imagePath = teacherRepository.findById(id)
                .map(Teacher::getImage)
                .orElse(null);

        if (imagePath == null || imagePath.isEmpty()) return null;

        Path path = Paths.get("G:/IdeaProjects/test_project/src/main/resources/static", imagePath);
        if (!Files.exists(path)) return null;

        return Files.readAllBytes(path);
    }

    public void saveTeacherImage(Integer id, MultipartFile file) throws IOException {
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

        teacherRepository.findById(id).ifPresent(s -> {
            s.setImage("images/" + fileName);
            teacherRepository.save(s);
        });
    }

    public void registerTeacher(Teacher teacher) throws Exception {
        if (teacher.getName() == null || teacher.getName().trim().isEmpty()) {
            throw new Exception("Name cannot be null or empty");
        }
        if (teacher.getSurname() == null || teacher.getSurname().trim().isEmpty()) {
            throw new Exception("Surname cannot be null or empty");
        }
        if (teacher.getEmail() == null || teacher.getEmail().trim().isEmpty()) {
            throw new Exception("Email cannot be null or empty");
        }
        if (teacher.getPassword() == null || teacher.getPassword().trim().isEmpty()) {
            throw new Exception("Password cannot be null or empty");
        }
        if (teacher.getPhoneNumber() == null || teacher.getPhoneNumber().trim().isEmpty()) {
            throw new Exception("Phone number cannot be null or empty");
        }

        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        teacher.setPhoneNumber(PhoneNumberUtil.addCountryCode(teacher.getCountry(), teacher.getPhoneNumber()));

        teacherRepository.save(teacher);
    }

}
