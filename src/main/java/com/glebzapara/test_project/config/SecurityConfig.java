package com.glebzapara.test_project.config;

import com.glebzapara.test_project.security.AdminDetails;
import com.glebzapara.test_project.security.StudentDetails;
import com.glebzapara.test_project.security.TeacherDetails;
import com.glebzapara.test_project.services.AdminDetailsService;
import com.glebzapara.test_project.services.StudentDetailsService;
import com.glebzapara.test_project.services.TeacherDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final AdminDetailsService adminDetailsService;
    private final StudentDetailsService studentDetailsService;
    private final TeacherDetailsService teacherDetailsService;


    public SecurityConfig(AdminDetailsService adminDetailsService,
                          StudentDetailsService studentDetailsService,
                          TeacherDetailsService teacherDetailsService) {
        this.adminDetailsService = adminDetailsService;
        this.studentDetailsService = studentDetailsService;
        this.teacherDetailsService = teacherDetailsService;
    }

    @Bean
    public DaoAuthenticationProvider adminAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(adminDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider studentAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(studentDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public DaoAuthenticationProvider teacherAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(teacherDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        return authentication -> {
            try {
                return adminAuthProvider().authenticate(authentication);
            } catch (Exception ignored) {
            }

            try {
                return studentAuthProvider().authenticate(authentication);
            } catch (Exception ignored) {
            }

            return teacherAuthProvider().authenticate(authentication);
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationManager(authenticationManager())
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/admins/**").hasRole("ADMIN")
                        .requestMatchers("/students/new").hasRole("ADMIN")
                        .requestMatchers("/students/**").hasAnyRole("ADMIN", "STUDENT")
                        .requestMatchers("/teachers/**").hasAnyRole("ADMIN", "TEACHER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .successHandler((request, response, authentication) -> {
                            Object principal = authentication.getPrincipal();

                            if (principal instanceof AdminDetails) {
                                response.sendRedirect("/");
                            } else if (principal instanceof StudentDetails s) {
                                response.sendRedirect("/students/" + s.getStudent().getId() + "/profile");
                            } else if (principal instanceof TeacherDetails t) {
                                response.sendRedirect("/teachers/" + t.getTeacher().getId() + "/profile");
                            }
                        })
                        .permitAll()
                );

        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
