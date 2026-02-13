package com.glebzapara.test_project.repositories;

import com.glebzapara.test_project.models.LessonGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonGroupRepository extends JpaRepository<LessonGroups, Integer> {
}
