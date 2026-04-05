package com.glebzapara.uniportal.config;

import com.glebzapara.uniportal.models.Student;
import com.glebzapara.uniportal.repositories.StudentRepository;
import com.glebzapara.uniportal.security.StudentDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.*;

@Component
public class LastSeenFilter extends OncePerRequestFilter {
    private final StudentRepository studentRepository;

    public LastSeenFilter(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.getPrincipal() instanceof StudentDetails studentDetails) {
            Student student = studentDetails.getStudent();

            ZonedDateTime now = ZonedDateTime.now(ZoneId.of("Europe/Kyiv"));

            if (student.getLastSeen() == null ||
                    student.getLastSeen().isBefore(now.minusMinutes(5))) {

                student.setLastSeen(now);
                studentRepository.save(student);
            }
        }

        filterChain.doFilter(request, response);
    }
}