package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.Group;
import com.glebzapara.uniportal.models.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    Teacher findById(Teacher teacher);
    Optional<Teacher> findByEmail(String email);
}
