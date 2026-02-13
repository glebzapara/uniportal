package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Department;
import com.glebzapara.test_project.models.Group;
import com.glebzapara.test_project.repositories.DepartmentRepository;
import com.glebzapara.test_project.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Optional<Department> findById(Integer id) {
        return departmentRepository.findById(id);
    }
}
