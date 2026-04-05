package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Department;
import com.glebzapara.uniportal.repositories.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department findById(Integer id) {
        return departmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Department not found"));
    }

    public void deleteById(Integer id) {
        departmentRepository.deleteById(id);
    }

    public void createDepartment(Department department) {
        departmentRepository.save(department);
    }
}
