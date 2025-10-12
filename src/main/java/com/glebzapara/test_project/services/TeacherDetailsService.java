package com.glebzapara.test_project.services;

import com.glebzapara.test_project.models.Teacher;
import com.glebzapara.test_project.repositories.TeacherRepository;
import com.glebzapara.test_project.security.TeacherDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TeacherDetailsService implements UserDetailsService {
    TeacherRepository teacherRepository;

    public TeacherDetailsService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Teacher teacher = teacherRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new TeacherDetails(teacher);
    }
}
