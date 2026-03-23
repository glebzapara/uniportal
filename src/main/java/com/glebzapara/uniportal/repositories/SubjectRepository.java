package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    List<Subject> findByDepartmentId(Integer departmentId);
}
