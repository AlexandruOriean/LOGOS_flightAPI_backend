package com.codecool.flightapi.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;

class AdminAccountInitializerTest {

    private static final String PASSWORD = "admin-Secret-123";

    private final UserRepository userRepository = mock(UserRepository.class);
    private final RoleRepository roleRepository = mock(RoleRepository.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
    private final ApplicationArguments args = mock(ApplicationArguments.class);

    private final Role userRole = new Role(1, RoleName.ROLE_USER);
    private final Role adminRole = new Role(2, RoleName.ROLE_ADMIN);

    private AdminAccountInitializer initializer(String username, String email, String password) {
        return new AdminAccountInitializer(
                new AdminAccountProperties(username, email, password), userRepository, roleRepository, passwordEncoder);
    }

    private void rolesExist() {
        when(roleRepository.findByName(RoleName.ROLE_USER)).thenReturn(Optional.of(userRole));
        when(roleRepository.findByName(RoleName.ROLE_ADMIN)).thenReturn(Optional.of(adminRole));
    }

    @Test
    void doesNothingWhenNoPasswordIsConfigured() {
        initializer("admin", "", "").run(args);

        verifyNoInteractions(userRepository, roleRepository, passwordEncoder);
    }

    @Test
    void createsAdminWithUserAndAdminRoles() {
        rolesExist();
        when(userRepository.getByUsername("admin")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(PASSWORD)).thenReturn("{bcrypt}hash");

        initializer("admin", "admin@example.com", PASSWORD).run(args);

        ArgumentCaptor<User> saved = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(saved.capture());
        assertThat(saved.getValue().getUsername()).isEqualTo("admin");
        assertThat(saved.getValue().getPassword()).isEqualTo("{bcrypt}hash");
        assertThat(saved.getValue().getRole())
                .extracting(Role::getName)
                .containsExactlyInAnyOrder(RoleName.ROLE_USER, RoleName.ROLE_ADMIN);
    }

    @Test
    void promotesExistingUserWithoutTouchingThePassword() {
        rolesExist();
        User existing = new User(
                5L, "Admin", "admin", "{bcrypt}original", "admin@example.com", new HashSet<>(Set.of(userRole)));
        when(userRepository.getByUsername("admin")).thenReturn(Optional.of(existing));

        initializer("admin", "admin@example.com", PASSWORD).run(args);

        assertThat(existing.getRole()).extracting(Role::getName).contains(RoleName.ROLE_ADMIN);
        assertThat(existing.getPassword()).isEqualTo("{bcrypt}original");
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void rejectsShortPassword() {
        assertThatThrownBy(
                        () -> initializer("admin", "admin@example.com", "short").run(args))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("at least 12 characters");
        verifyNoInteractions(userRepository);
    }

    @Test
    void requiresUsernameAndEmailWhenPasswordIsSet() {
        assertThatThrownBy(() -> initializer("admin", "", PASSWORD).run(args))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("app.admin.email");
    }

    @Test
    void neverExposesThePasswordInToString() {
        assertThat(new AdminAccountProperties("admin", "admin@example.com", PASSWORD).toString())
                .doesNotContain(PASSWORD)
                .contains("****");
    }
}
