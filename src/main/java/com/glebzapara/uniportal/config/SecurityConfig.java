package com.glebzapara.uniportal.config;

import com.glebzapara.uniportal.security.AdminDetails;
import com.glebzapara.uniportal.security.StudentDetails;
import com.glebzapara.uniportal.security.TeacherDetails;
import com.glebzapara.uniportal.services.AdminDetailsService;
import com.glebzapara.uniportal.services.StudentDetailsService;
import com.glebzapara.uniportal.services.TeacherDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AdminDetailsService adminDetailsService;
    private final StudentDetailsService studentDetailsService;
    private final TeacherDetailsService teacherDetailsService;
    private final LastSeenFilter lastSeenFilter;

    public SecurityConfig(AdminDetailsService adminDetailsService,
                          StudentDetailsService studentDetailsService,
                          TeacherDetailsService teacherDetailsService,
                          LastSeenFilter lastSeenFilter) {
        this.adminDetailsService = adminDetailsService;
        this.studentDetailsService = studentDetailsService;
        this.teacherDetailsService = teacherDetailsService;
        this.lastSeenFilter = lastSeenFilter;
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
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(adminAuthProvider())
                .authenticationProvider(studentAuthProvider())
                .authenticationProvider(teacherAuthProvider())
                .csrf(csrf -> csrf.disable())
                .addFilterAfter(lastSeenFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/admins/**").hasRole("SUPER_ADMIN")
                        .requestMatchers("/students/new").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/teachers/new").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/groups/new").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/grades/new").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/departments/new").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/lessons/new").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/subjects/new").hasAnyRole("SUPER_ADMIN", "ADMIN")
                        .requestMatchers("/students/**").hasAnyRole("SUPER_ADMIN", "ADMIN",
                                                                                "STUDENT", "TEACHER")
                        .requestMatchers("/teachers/**").hasAnyRole("SUPER_ADMIN", "ADMIN",
                                                                                    "STUDENT", "TEACHER")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .failureUrl("/login?error")
                        .successHandler((request,
                                         response,
                                         authentication) -> {
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