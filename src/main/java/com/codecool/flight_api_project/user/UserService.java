package com.codecool.flight_api_project.user;

import com.codecool.flight_api_project.common.error.ResourceAlreadyExistsException;
import java.util.HashSet;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user.
     *
     * @throws ResourceAlreadyExistsException if the username or e-mail is already taken
     */
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username is already taken.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("E-mail is already registered.");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setRole(resolveRoles(request.getRoles()));
        userRepository.save(user);
    }

    // TODO(REFACTORING_ROADMAP task 5): clients must not be able to self-assign ROLE_ADMIN.
    private Set<UserRole> resolveRoles(Set<String> requestedRoles) {
        Set<UserRole> roles = new HashSet<>();
        if (requestedRoles == null) {
            roles.add(findRole(UserRoleEnum.ROLE_USER));
        } else {
            requestedRoles.forEach(role ->
                    roles.add(findRole("admin".equals(role) ? UserRoleEnum.ROLE_ADMIN : UserRoleEnum.ROLE_USER)));
        }
        return roles;
    }

    private UserRole findRole(UserRoleEnum name) {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new IllegalStateException("Role " + name + " is missing from the database"));
    }
}
