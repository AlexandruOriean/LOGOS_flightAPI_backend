package com.codecool.flightapi.auth;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class RegisterRequest {
    private String username, name, email, password;
    //            confirmPassword;
    private Set<String> roles;
}
