package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Student;
import com.glebzapara.test_project.models.Subject;
import com.glebzapara.test_project.repositories.StudentRepository;
import com.glebzapara.test_project.util.PhoneNumberUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class StudentService {
    StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
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

    public Integer getStudentFacultyById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getFaculty)
                .orElseThrow(() -> new Exception("Faculty cannot be null"));
    }

    public Short getStudentCourseById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getCourse)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

    public String getStudentSpecialityById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getSpeciality)
                .orElseThrow(() -> new Exception("Speciality cannot be null"));
    }

    public String getStudentCountryById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getCountry)
                .orElseThrow(() -> new Exception("Country cannot be null"));
    }

    public String getStudentPhoneNumberById(Integer id) throws Exception {
        return studentRepository.findById(id)
                .map(Student::getPhoneNumber)
                .orElseThrow(() -> new Exception("Phone number cannot be null"));
    }

    public byte[] getStudentImageById(Integer id) throws IOException {
        String imagePath = studentRepository.findById(id)
                .map(Student::getImage)
                .orElse(null);

        if (imagePath == null || imagePath.isEmpty()) return null;

        Path path = Paths.get("G:/IdeaProjects/test_project/src/main/resources/static", imagePath);
        if (!Files.exists(path)) return null;

        return Files.readAllBytes(path);
    }

    public void saveStudentImage(Integer id, MultipartFile file) throws IOException {
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

        studentRepository.findById(id).ifPresent(s -> {
            s.setImage("images/" + fileName);
            studentRepository.save(s);
        });
    }


    public void registerStudent(Student student) throws Exception {
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
        if (student.getFaculty() == null || student.getFaculty() < 1 || student.getFaculty() > 8) {
            throw new Exception("Faculty must be between 1 and 8");
        }
        if (student.getSpeciality() == null || student.getSpeciality().trim().isEmpty()) {
            throw new Exception("Speciality cannot be null or empty");
        }
        if (student.getPhoneNumber() == null || student.getPhoneNumber().trim().isEmpty()) {
            throw new Exception("Phone number cannot be null or empty");
        }

        student.setPhoneNumber(PhoneNumberUtil.addCountryCode(student.getCountry(), student.getPhoneNumber()));

        studentRepository.save(student);
    }
}
