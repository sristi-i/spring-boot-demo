package com.springboot.demo.controller;

import com.springboot.demo.entity.User;
import com.springboot.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired private UserService userService;

    // PUBLIC — no JWT needed
    // SecurityConfig: .requestMatchers("/api/users/register").permitAll()
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User saved = userService.registerUser(user); // BCrypt encoding happens inside
        saved.setPassword("[PROTECTED]");            // NEVER return password in response
        return ResponseEntity.ok(saved);
    }

    // PROTECTED — requires valid JWT in Authorization header
    // JwtAuthenticationFilter already validated token + set SecurityContext
    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        // read username from SecurityContext — set by JWT filter earlier in chain
        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName(); // returns "sub" claim from the JWT = username
        return ResponseEntity.ok("Welcome " + username + "! JWT is valid.");
    }
}