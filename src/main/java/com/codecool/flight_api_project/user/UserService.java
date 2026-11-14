package com.codecool.flight_api_project.user;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    UserRoleRepository roleRepository;

    public Optional<User> getUserByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    public User getUserById(long userId) {
        return userRepository.getById(userId);
    }

    public User getUserByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    public ResponseEntity<?> register(RegisterRequest request) {
        ResponseEntity<?> validation = validateRegister(request);
        if (validation.getStatusCode().equals(HttpStatus.OK)) {
            User newUser = createUser(request);
            Set<String> strRoles = request.getRoles();
            Set<UserRole> roles = new HashSet<>();

            if (strRoles == null) {
                UserRole userRole = roleRepository
                        .findByName(UserRoleEnum.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    if ("admin".equals(role)) {
                        UserRole adminRole = roleRepository
                                .findByName(UserRoleEnum.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
                    } else {
                        UserRole userRole = roleRepository
                                .findByName(UserRoleEnum.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                });
            }
            newUser.setRole(roles);

            userRepository.save(newUser);
        }
        return validation;
    }

    private User createUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());

        return user;
    }

    private ResponseEntity<?> validateRegister(RegisterRequest request) {
        Optional<User> userName = getUserByUsername(request.getUsername());
        User email = getUserByEmail(request.getEmail());
        if (userRepository.existsByUsername(request.getUsername())) {
            return new ResponseEntity<>("Username already in use", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email already in use", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        //        if (!request.getPassword().equals(request.getConfirmPassword())) {
        //            return new ResponseEntity<>("Password don't match", HttpStatus.INTERNAL_SERVER_ERROR);
        //        }
        return new ResponseEntity<>("Registration successful", HttpStatus.OK);
    }
}
