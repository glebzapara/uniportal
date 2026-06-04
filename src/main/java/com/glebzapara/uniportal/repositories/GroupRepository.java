package com.glebzapara.uniportal.repositories;

import com.glebzapara.uniportal.models.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}