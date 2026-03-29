package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.Grade;
import com.glebzapara.uniportal.models.Group;
import com.glebzapara.uniportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    Optional<Student> findByEmail(String email);
    List<Student> findByGroup(Group group);
    List<Student> findByGroupId(Integer groupId);
}
