package com.felipecordeiro.dscatalog.config;

import com.felipecordeiro.dscatalog.entities.Role;
import com.felipecordeiro.dscatalog.entities.User;
import com.felipecordeiro.dscatalog.repositories.RoleRepository;
import com.felipecordeiro.dscatalog.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByAuthority("ROLE_ADMIN");

            if (adminRole == null) {
                adminRole = new Role();
                adminRole.setAuthority("ROLE_ADMIN");
                roleRepository.save(adminRole);
            }

            User admin = new User();
            admin.setFirstName("Admin");
            admin.setLastName("Root");
            admin.setEmail("admin@dscatalog.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.getRoles().add(adminRole);

            userRepository.save(admin);
            System.out.println("Admin created successfully: admin@dscatalog.com / admin123");
        }
    }
}
