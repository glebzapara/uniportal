package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Student;
import com.glebzapara.test_project.models.Subject;
import com.glebzapara.test_project.repositories.SubjectRepository;
import com.glebzapara.test_project.util.PhoneNumberUtil;
import org.springframework.stereotype.Service;

@Service
public class SubjectService {
    SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public String getSubjectNameById(Integer id) throws Exception {
        return subjectRepository.findById(id)
                .map(Subject::getName)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

    public Integer getSubjectFacultyById(Integer id) throws Exception {
        return subjectRepository.findById(id)
                .map(Subject::getFaculty)
                .orElseThrow(() -> new Exception("Faculty cannot be null"));
    }

    public Short getSubjectCourseById(Integer id) throws Exception {
        return subjectRepository.findById(id)
                .map(Subject::getCourse)
                .orElseThrow(() -> new Exception("Name cannot be null"));
    }

    public String getSubjectSpecialityById(Integer id) throws Exception {
        return subjectRepository.findById(id)
                .map(Subject::getSpeciality)
                .orElseThrow(() -> new Exception("Speciality cannot be null"));
    }

    public void registerSubject(Subject subject) throws Exception {
        if (subject.getName() == null || subject.getName().trim().isEmpty()) {
            throw new Exception("Name cannot be null or empty");
        }
        if (subject.getFaculty() == null || subject.getFaculty() < 1 || subject.getFaculty() > 8) {
            throw new Exception("Faculty must be between 1 and 8");
        }
        if (subject.getSpeciality() == null || subject.getSpeciality().trim().isEmpty()) {
            throw new Exception("Speciality cannot be null or empty");
        }

        subjectRepository.save(subject);
    }
}
