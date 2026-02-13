package com.glebzapara.test_project;

import com.glebzapara.test_project.models.Admin;
import com.glebzapara.test_project.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        String email = "gleb.zapara2008@gmail.com";

        adminRepository.findByEmail(email).ifPresentOrElse(
                existingAdmin -> {
                    System.out.println(">>> ADMIN ALREADY EXISTS <<<");
                },
                () -> {
                    Admin admin = new Admin();
                    admin.setName("Gleb");
                    admin.setSurname("Zapara");
                    admin.setEmail(email);
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.setRole("ROLE_ADMIN");

                    adminRepository.save(admin);

                    System.out.println(">>> ADMIN CREATED <<<");
                }
        );
    }
}
