package com.codecool.flight_api_project.user;


import com.codecool.flight_api_project.payload.response.JwtResponse;
import com.codecool.flight_api_project.security.jwt.JwtUtils;
import com.codecool.flight_api_project.security.service.UserDetailsImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserRoleRepository roleRepository;


    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;

//    @GetMapping("/get-user/{username}")
//    public User returnUser(@PathVariable String username ) {
//        return userService.getUserByUsername(username);
//    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid  @RequestBody RegisterRequest request) {
         return userService.register(request);

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            Cookie cookie = new Cookie("token",jwt);
            cookie.setMaxAge(7 * 24 * 60 * 60);

            response.addCookie(cookie);

            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getEmail(),
                    roles));

    }

    @GetMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("token","NO_TOKEN");
        cookie.setMaxAge(7 * 24 * 60 * 60);
        return new ResponseEntity<>(HttpStatus.OK);

    }



}
