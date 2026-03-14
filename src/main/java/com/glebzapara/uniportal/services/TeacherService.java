package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Department;
import com.glebzapara.uniportal.models.Teacher;
import com.glebzapara.uniportal.repositories.DepartmentRepository;
import com.glebzapara.uniportal.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeacherService {
    TeacherRepository teacherRepository;
    DepartmentRepository departmentRepository;
    private PasswordEncoder passwordEncoder;
    private final S3Client s3Client;

    @Value("${r2.bucket}")
    private String bucket;

    @Value("${r2.public-url}")
    private String publicUrl;

    public TeacherService(TeacherRepository teacherRepository,
                          DepartmentRepository departmentRepository,
                          PasswordEncoder passwordEncoder,
                          S3Client s3Client) {

        this.teacherRepository = teacherRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    public List<Teacher> findAllTeachers() {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> findOneTeacher(Integer id) {
        return teacherRepository.findById(id);
    }

    public String getTeacherImageById(Integer id) {
        return teacherRepository.findById(id)
                .map(Teacher::getImage)
                .orElse(null);
    }

    public void saveTeacherImage(Integer id, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return;
        }

        String key = "teachers/" + id + "/" + UUID.randomUUID();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        String imageUrl = publicUrl + "/" + key;

        teacherRepository.findById(id).ifPresent(t -> {
            t.setImage(imageUrl);
            teacherRepository.save(t);
        });
    }

    public void registerTeacher(Teacher teacher, Integer departmentId) throws Exception {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new IllegalArgumentException("Department not found"));

        teacher.setDepartment(department);

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

        teacherRepository.save(teacher);
    }
}
