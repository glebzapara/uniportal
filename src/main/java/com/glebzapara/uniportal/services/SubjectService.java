package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Subject;
import com.glebzapara.uniportal.repositories.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject findById(Integer id) throws Exception {
        return subjectRepository.findById(id).orElseThrow(() -> new Exception("Subject not found"));
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

    public List<Subject> findByDepartmentId(Integer departmentId) {
        return subjectRepository.findByDepartmentId(departmentId);
    }

    public void registerSubject(Subject subject) throws Exception {
        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            throw new Exception("Name cannot be null or empty");
        }

        subjectRepository.save(subject);
    }
}
