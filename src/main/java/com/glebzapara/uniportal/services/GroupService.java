package com.glebzapara.uniportal.services;

import com.glebzapara.uniportal.models.Department;
import com.glebzapara.uniportal.models.Group;
import com.glebzapara.uniportal.repositories.DepartmentRepository;
import com.glebzapara.uniportal.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;
    private final DepartmentRepository departmentRepository;

    public GroupService(GroupRepository groupRepository, DepartmentRepository departmentRepository) {
        this.groupRepository = groupRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<Group> findAllGroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id);
    }

    public void createGroup(Group group, Integer departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        group.setDepartment(department);
        groupRepository.save(group);
    }
}
