package com.librarymanagementsystem.common.config;


import com.librarymanagementsystem.user.entity.AppRole;
import com.librarymanagementsystem.user.entity.Role;
import com.librarymanagementsystem.user.entity.User;
import com.librarymanagementsystem.user.repository.RoleRepository;
import com.librarymanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {

            // 1. Roles insert
            for (AppRole appRole : AppRole.values()) {
                if (!roleRepository.existsByRoleName(appRole)) {
                    Role role = new Role();
                    role.setRoleName(appRole);
                    roleRepository.save(role);
                }
            }

            // 2. Default admin insert
            if (!userRepository.existsByUsername("admin")) {

                Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("ROLE_ADMIN not found"));

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setEnabled(true);
                admin.setRoles(Set.of(adminRole));

                userRepository.save(admin);
            }
            // 2. Default admin insert
            if (!userRepository.existsByUsername("librarian")) {

                Role librarianRole = roleRepository.findByRoleName(AppRole.ROLE_LIBRARIAN)
                        .orElseThrow(() -> new RuntimeException("ROLE_LIBRARIAN not found"));

                User librarian = new User();
                librarian.setUsername("librarian");
                librarian.setEmail("librarian@gmail.com");
                librarian.setPassword(passwordEncoder.encode("Librarian@123"));
                librarian.setEnabled(true);
                librarian.setRoles(Set.of(librarianRole));

                userRepository.save(librarian);
            }
        };
    }
}