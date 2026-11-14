package com.codecool.flight_api_project.user;

import com.codecool.flight_api_project.payload.response.JwtResponse;
import com.codecool.flight_api_project.payload.response.MessageResponse;
import com.codecool.flight_api_project.security.jwt.JwtTokenService;
import com.codecool.flight_api_project.security.service.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private static final String TOKEN_COOKIE = "token";
    private static final int TOKEN_COOKIE_MAX_AGE_SECONDS = 7 * 24 * 60 * 60;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UserService userService;

    /** 201 on success; 409 if the username or e-mail is taken. */
    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@Valid @RequestBody RegisterRequest request) {
        userService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(new MessageResponse("Registration successful"));
    }

    /** 200 with the access token; 401 (problem+json) on bad credentials. */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(
            @Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        String jwt = jwtTokenService.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        Cookie cookie = new Cookie(TOKEN_COOKIE, jwt);
        cookie.setMaxAge(TOKEN_COOKIE_MAX_AGE_SECONDS);
        response.addCookie(cookie);

        return ResponseEntity.ok(
                new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    /** Clears the token cookie. (Cookie hardening and POST-only logout: REFACTORING_ROADMAP task 9.) */
    @GetMapping("/logout")
    public ResponseEntity<Void> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie(TOKEN_COOKIE, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.noContent().build();
    }
}
