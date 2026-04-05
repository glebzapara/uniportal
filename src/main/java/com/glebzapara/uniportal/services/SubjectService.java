package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Department;
import com.glebzapara.uniportal.models.Student;
import com.glebzapara.uniportal.models.Subject;
import com.glebzapara.uniportal.repositories.DepartmentRepository;
import com.glebzapara.uniportal.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubjectService {
    SubjectRepository subjectRepository;
    DepartmentRepository departmentRepository;

    public SubjectService(SubjectRepository subjectRepository,
                          DepartmentRepository departmentRepository) {
        this.subjectRepository = subjectRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject findById(Integer id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
    }

    public void deleteById(Integer id) {
        subjectRepository.deleteById(id);
    }

    public String getSubjectNameById(Integer id) throws Exception {
        return subjectRepository.findById(id)
                .map(Subject::getName)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

    public Short getSubjectCourseById(Integer id) throws Exception {
        return subjectRepository.findById(id)
                .map(Subject::getCourse)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

//    public List<Subject> findByDepartmentId(Integer departmentId) {
//        return subjectRepository.findByDepartmentId(departmentId);
//    }

    public String extract(String text, String key) {
        if (text == null) {
            return "";
        }

        int start = text.indexOf(key);
        if (start == -1) {
            return "";
        }

        start += key.length();

        int next = text.indexOf("#", start);
        if (next == -1) {
            next = text.length();
        }

        return text.substring(start, next).trim();
    }

    public void registerSubject(Subject subject, Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        subject.setDepartment(department);

        subjectRepository.save(subject);
    }
}
