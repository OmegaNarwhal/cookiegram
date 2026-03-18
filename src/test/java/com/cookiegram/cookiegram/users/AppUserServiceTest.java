package com.cookiegram.cookiegram.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AppUserServiceTest {

    private AppUserRepository appUserRepository;
    private PasswordEncoder passwordEncoder;
    private AppUserService appUserService;

    @BeforeEach
    void setUp() {
        appUserRepository = mock(AppUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        appUserService = new AppUserService(appUserRepository, passwordEncoder);
    }

    @Test
    void registerCustomer_shouldSaveUser() {
        RegisterForm form = new RegisterForm();
        form.setFullName("John Smith");
        form.setEmail("john@test.com");
        form.setPassword("secret123");
        form.setConfirmPassword("secret123");

        when(appUserRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("ENCODED_PASSWORD");

        appUserService.registerCustomer(form);

        verify(appUserRepository).save(argThat(user ->
                user.getFullName().equals("John Smith") &&
                user.getEmail().equals("john@test.com") &&
                user.getPassword().equals("ENCODED_PASSWORD") &&
                user.getRole().equals("ROLE_CUSTOMER")
        ));
    }

    @Test
    void registerCustomer_shouldThrowIfEmailExists() {
        RegisterForm form = new RegisterForm();
        form.setFullName("John Smith");
        form.setEmail("john@test.com");
        form.setPassword("secret123");
        form.setConfirmPassword("secret123");

        when(appUserRepository.existsByEmail("john@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> appUserService.registerCustomer(form)
        );

        assertEquals("An account with that email already exists.", ex.getMessage());
        verify(appUserRepository, never()).save(any());
    }

    @Test
    void registerCustomer_shouldThrowIfPasswordsDoNotMatch() {
        RegisterForm form = new RegisterForm();
        form.setFullName("John Smith");
        form.setEmail("john@test.com");
        form.setPassword("secret123");
        form.setConfirmPassword("different123");

        when(appUserRepository.existsByEmail("john@test.com")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> appUserService.registerCustomer(form)
        );

        assertEquals("Passwords do not match.", ex.getMessage());
        verify(appUserRepository, never()).save(any());
    }
}