package com.librarymanagementsystem.common.config;

import com.librarymanagementsystem.user.entity.AppRole;
import com.librarymanagementsystem.user.entity.Role;
import com.librarymanagementsystem.user.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {


    private final RoleRepository roleRepository;

    @Bean
    CommandLineRunner initRoles() {
        return args -> {
            for (AppRole appRole : AppRole.values()) {
                if (!roleRepository.existsByRoleName(appRole)) {
                    Role role = new Role();
                    role.setRoleName(appRole);
                    roleRepository.save(role);
                }
            }
        };
    }
}
