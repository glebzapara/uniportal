package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Group;
import com.glebzapara.test_project.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> findAll() {
        return groupRepository.findAll();
    }

    public Optional<Group> findById(Integer id) {
        return groupRepository.findById(id);
    }
}
