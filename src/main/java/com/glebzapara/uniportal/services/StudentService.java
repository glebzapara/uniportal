package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Student;
import com.glebzapara.uniportal.models.Group;
import com.glebzapara.uniportal.repositories.GroupRepository;
import com.glebzapara.uniportal.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.*;


@Service
public class StudentService {
    StudentRepository studentRepository;
    GroupRepository groupRepository;
    private PasswordEncoder passwordEncoder;
    private final S3Client s3Client;

    @Value("${r2.bucket}")
    private String bucket;

    @Value("${r2.public-url}")
    private String publicUrl;

    public StudentService(StudentRepository studentRepository,
                          GroupRepository groupRepository,
                          PasswordEncoder passwordEncoder, S3Client s3Client) {
        this.studentRepository = studentRepository;
        this.groupRepository = groupRepository;
        this.passwordEncoder = passwordEncoder;
        this.s3Client = s3Client;
    }

    public List<Student> findAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> findOneStudent(Integer id) {
        return studentRepository.findById(id);
    }

    public List<Student> findByGroup(Group group) {
        return studentRepository.findByGroup(group);
    }

    public void saveStudentImage(Integer id, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return;
        }

        String key = "students/" + id + "/" + UUID.randomUUID();

        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        String imageUrl = publicUrl + "/" + key;

        studentRepository.findById(id).ifPresent(s -> {
            s.setImage(imageUrl);
            studentRepository.save(s);
        });
    }

    public void registerStudent(Student student, Integer groupId) throws Exception {
        try {
            Group studentGroup = groupRepository.findById(groupId)
                    .orElseThrow(() -> new Exception("Group not found"));

            student.setGroup(studentGroup);

            if (student.getName() == null || student.getName().trim().isEmpty()) {
                throw new Exception("Name cannot be null or empty");
            }

            if (student.getSurname() == null || student.getSurname().trim().isEmpty()) {
                throw new Exception("Surname cannot be null or empty");
            }

            if (student.getEmail() == null || student.getEmail().trim().isEmpty()) {
                throw new Exception("Email cannot be null or empty");
            }

            if (student.getPassword() == null || student.getPassword().trim().isEmpty()) {
                throw new Exception("Password cannot be null or empty");
            }

            if (studentGroup.getCourse() == null
                    || studentGroup.getCourse() < 1
                    || studentGroup.getCourse() > 6) {
                throw new Exception("Course must be between 1 and 6");
            }

            if (studentGroup.getSpeciality() == null || studentGroup.getSpeciality().trim().isEmpty()) {
                throw new Exception("Speciality cannot be null or empty");
            }

            if (student.getPhoneNumber() == null || student.getPhoneNumber().trim().isEmpty()) {
                throw new Exception("Phone number cannot be null or empty");
            }

            student.setPassword(passwordEncoder.encode(student.getPassword()));

            studentRepository.save(student);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
