package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Student;
import com.glebzapara.test_project.models.Group;
import com.glebzapara.test_project.repositories.GroupRepository;
import com.glebzapara.test_project.repositories.StudentRepository;
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

    public String getStudentNameById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getName)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

    public String getStudentSurnameById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getSurname)
                .orElseThrow(() -> new Exception("Surname cannot be null"));
    }

    public String getStudentEmailById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getEmail)
                .orElseThrow(() -> new Exception("Email cannot be null"));
    }

    public String getStudentPasswordById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map((Student::getPassword))
                .orElseThrow(() -> new Exception("Password cannot be null"));
    }

    public Group getStudentGroupById(Integer studentId) throws Exception {
        return studentRepository.findById(studentId)
                .map(Student::getGroup)
                .orElseThrow(() -> new Exception("Group cannot be null"));
    }

    public Short getStudentGroupCourseById(Integer studentId) throws Exception {
        return getStudentGroupById(studentId).getCourse();
    }

    public String getStudentGroupSpecialityById(Integer studentId) throws Exception {
        return getStudentGroupById(studentId).getSpeciality();
    }

    public String getStudentPhoneNumberById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getPhoneNumber)
                .orElseThrow(() -> new Exception("Phone number cannot be null"));
    }

    public String getStudentImageById(Integer id) {
        return studentRepository.findById(id)
                .map(Student::getImage)
                .orElse(null);

//        String imagePath = studentRepository.findById(id)
//                .map(Student::getImage)
//                .orElse(null);
//
//        if (imagePath == null || imagePath.isEmpty()) return null;
//
//        Path path = Paths.get("G:/IdeaProjects/test_project/src/main/resources/static", imagePath);
//        if (!Files.exists(path)) return null;
//
//        return Files.readAllBytes(path);
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

            student.setPhoneNumber(student.getPhoneNumber());

            studentRepository.save(student);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
