package com.glebzapara.test_project.repositories;

import com.glebzapara.test_project.models.LessonStudentScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonStudentScoreRepository extends JpaRepository<LessonStudentScore, Integer> {
}


