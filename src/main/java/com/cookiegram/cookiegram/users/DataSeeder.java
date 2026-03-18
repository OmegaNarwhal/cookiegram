package com.cookiegram.cookiegram.users;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedUser("employee@cookiegram.com", "Employee User", "pass", "ROLE_EMPLOYEE");
        seedUser("admin@cookiegram.com", "Admin User", "pass", "ROLE_ADMIN");
    }

    private void seedUser(String email, String fullName, String rawPassword, String role) {
        if (appUserRepository.existsByEmail(email)) {
            return;
        }

        AppUser user = new AppUser();
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole(role);

        appUserRepository.save(user);
    }
}
