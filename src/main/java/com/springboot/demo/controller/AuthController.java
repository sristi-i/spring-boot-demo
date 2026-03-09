package com.springboot.demo.controller;

import com.springboot.demo.dto.JwtResponse;
import com.springboot.demo.dto.LoginRequest;
import com.springboot.demo.service.JwtService;
import com.springboot.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UserService userService;
    @Autowired private JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // @RequestBody = Jackson maps incoming JSON → LoginRequest object

        try {
            // triggers: DaoAuthenticationProvider
            //   → userService.loadUserByUsername(username)
            //   → BCrypt.matches(rawPassword, storedHash)
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),  // from JSON body
                    request.getPassword()   // from JSON body
                ));
        } catch (BadCredentialsException e) {
            // wrong username OR wrong password
            // 401 = not authenticated (bad credentials)
            // 403 = authenticated but no permission (wrong role)
            return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
        }

        // credentials verified → generate JWT
        UserDetails userDetails =
            userService.loadUserByUsername(request.getUsername());
        String token = jwtService.generateToken(userDetails);

        // collect roles to include in response (ROLE_ADMIN, ROLE_USER etc.)
        List<String> roles = userDetails.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.toList());

        // 200 OK with full JwtResponse: token + type + username + roles
        return ResponseEntity.ok(
            new JwtResponse(token, userDetails.getUsername(), roles));
    }
}