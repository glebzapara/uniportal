package com.glebzapara.test_project.repositories;

import com.glebzapara.test_project.models.StudentSubjectScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentSubjectScoreRepository extends JpaRepository<StudentSubjectScore, Integer> {
}
