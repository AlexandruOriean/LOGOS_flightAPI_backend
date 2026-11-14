package com.codecool.flightapi.user;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

/**
 * The administrator account provisioned at startup, e.g. from the environment variables
 * {@code APP_ADMIN_USERNAME}, {@code APP_ADMIN_EMAIL} and {@code APP_ADMIN_PASSWORD}.
 * Provisioning is skipped when no password is configured.
 */
@ConfigurationProperties("app.admin")
public record AdminAccountProperties(String username, String email, String password) {

    /** Minimum length for the configured admin password. */
    public static final int MIN_PASSWORD_LENGTH = 12;

    public boolean isConfigured() {
        return StringUtils.hasText(password);
    }

    @Override
    public String toString() {
        // never expose the password, e.g. in logs or actuator output
        return "AdminAccountProperties[username=" + username + ", email=" + email + ", password=****]";
    }
}
