package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.Group;
import com.glebzapara.uniportal.models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends JpaRepository<Group, Integer> {
    List<Group> findById(Group group);
}