package com.codecool.flightapi.user;

import java.util.HashSet;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Provisions the administrator account from {@link AdminAccountProperties} at startup. This is the
 * only way to obtain {@link RoleName#ROLE_ADMIN}: self-registration always creates plain users.
 *
 * <ul>
 *   <li>Not configured (no password): nothing happens.
 *   <li>User does not exist: it is created with ROLE_USER and ROLE_ADMIN.
 *   <li>User exists: ROLE_ADMIN is added if missing. The password is never overwritten.
 * </ul>
 */
@Component
public class AdminAccountInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private final AdminAccountProperties admin;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminAccountInitializer(
            AdminAccountProperties admin,
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {
        this.admin = admin;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (!admin.isConfigured()) {
            log.info("No admin account configured (app.admin.password is empty); skipping provisioning");
            return;
        }
        validate();

        userRepository.getByUsername(admin.username()).ifPresentOrElse(this::ensureAdminRole, this::createAdmin);
    }

    private void validate() {
        if (!StringUtils.hasText(admin.username()) || !StringUtils.hasText(admin.email())) {
            throw new IllegalStateException(
                    "app.admin.username and app.admin.email are required with app.admin.password");
        }
        if (admin.password().length() < AdminAccountProperties.MIN_PASSWORD_LENGTH) {
            throw new IllegalStateException("app.admin.password must be at least "
                    + AdminAccountProperties.MIN_PASSWORD_LENGTH + " characters");
        }
    }

    private void createAdmin() {
        User user = new User();
        user.setUsername(admin.username());
        user.setEmail(admin.email());
        user.setName(admin.username());
        user.setPassword(passwordEncoder.encode(admin.password()));
        user.setRole(new HashSet<>(Set.of(findRole(RoleName.ROLE_USER), findRole(RoleName.ROLE_ADMIN))));
        userRepository.save(user);
        log.info("Created admin account '{}'", admin.username());
    }

    private void ensureAdminRole(User user) {
        boolean isAdmin = user.getRole().stream().anyMatch(role -> role.getName() == RoleName.ROLE_ADMIN);
        if (isAdmin) {
            log.info("Admin account '{}' already exists", user.getUsername());
            return;
        }
        user.getRole().add(findRole(RoleName.ROLE_ADMIN));
        log.warn("Granted ROLE_ADMIN to existing user '{}' (configured as app.admin.username)", user.getUsername());
    }

    private Role findRole(RoleName name) {
        return roleRepository
                .findByName(name)
                .orElseThrow(() -> new IllegalStateException("Role " + name + " is missing from the database"));
    }
}
