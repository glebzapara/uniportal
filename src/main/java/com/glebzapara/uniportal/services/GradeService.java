package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Department;
import com.glebzapara.uniportal.models.Grade;
import com.glebzapara.uniportal.repositories.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository groupRepository) {
        this.gradeRepository = groupRepository;
    }

    public List<Grade> findAllGrades() {
        return gradeRepository.findAll();
    }

    public Grade findById(Integer id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade not found"));
    }

    public void deleteById(Integer id) {
        gradeRepository.deleteById(id);
    }

    public List<Grade> findByStudentId(Integer studentId) {
        return gradeRepository.findByStudentId(studentId);
    }

    public void createGrade(Grade grade) {
        gradeRepository.save(grade);
    }
}