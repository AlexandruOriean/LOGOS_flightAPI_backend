package com.codecool.flightapi.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Self-registration payload. Deliberately has no roles: every self-registered account is a plain
 * user. Admin accounts are provisioned by {@link com.codecool.flightapi.user.AdminAccountInitializer}.
 */
@AllArgsConstructor
@Setter
@Getter
public class RegisterRequest {
    private String username;
    private String name;
    private String email;
    private String password;
}
