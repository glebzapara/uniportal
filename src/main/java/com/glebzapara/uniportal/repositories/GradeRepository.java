package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    List<Grade> findByStudentId(Integer studentId);
    List<Grade> findBySubjectId(Integer subjectId);

    Optional<Grade> findByStudentIdAndSubjectId(Integer studentId, Integer subjectId);
}
