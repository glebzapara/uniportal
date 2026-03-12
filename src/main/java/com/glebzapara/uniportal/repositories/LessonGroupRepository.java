package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.LessonGroups;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LessonGroupRepository extends JpaRepository<LessonGroups, Integer> {
}
