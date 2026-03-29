package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Grade;
import com.glebzapara.uniportal.models.Group;
import com.glebzapara.uniportal.repositories.GradeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GradeService {
    private final GradeRepository gradeRepository;

    public GradeService(GradeRepository groupRepository) {
        this.gradeRepository = groupRepository;
    }

    public List<Grade> findAllGrades() {
        return gradeRepository.findAll();
    }

    public Optional<Grade> findById(Integer id) {
        return gradeRepository.findById(id);
    }

    public List<Grade> findByStudentId(Integer studentId) {
        return gradeRepository.findByStudentId(studentId);
    }
}