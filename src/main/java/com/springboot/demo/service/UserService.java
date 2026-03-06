package com.springboot.demo.service;

import com.springboot.demo.entity.User;
import com.springboot.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
// implements UserDetailsService - spring security interface
// tells Spring: "use this to load users from database during authentication"
public class UserService implements UserDetailsService{
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    // called by spring security during authentication 
    @Override
    public UserDetails loadUserByUsername(String username)
        throws UsernameNotFoundException{
            // find user in db - orElsethorow: if not found spring catches - 401
            User user = userRepository.findByUsername(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
            
            // "ADMIN" -> "ROLE_ADMIN"
            // spring security requires ROLE_prefix for role-based authorization
            List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                                                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                                            .collect(Collectors.toList());
            
            // return spring's UserDetails (not our User entity)
            // spring uses this to verify password and read roles
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

        }
    
    // register new user
    public User registerUser(User user){
        // always encode password before saving - never store plain text
        user.setPassword((passwordEncoder.encode(user.getPassword())));
        return userRepository.save(user); // Hibernate saves to users + user_roles tables
    }
}
