package com.springboot.demo.service;

import com.springboot.demo.entity.User;
import com.springboot.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.springboot.demo.exception.ResourceNotFoundException;


import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import com.springboot.demo.dto.UserDTO;

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

    @Autowired
    private ModelMapper modelMapper;

    // Entity -> DTO (for response, hide password)
    public UserDTO getUserById(Long id){
        // Before: orElseThrow(() -> new RuntimeException("User not found"))
        // returns 500 internal servr error
        // User user = userRepository.findById(id).orElseThrow(
        //     () -> new RuntimeException("User not found"));

        // after: throw ResourceNotFoundException
        // GlobalExceptionHandler catches it -> returns clean 404 JSON
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

        // modelMapper.map(source, destinationType.class)
        // Auto-maps fields with SAME name. Different names need nmanual config
        return modelMapper.map(user, UserDTO.class);
    }

    // DTO -> Entity (for saving incoming request)
    public UserDTO savUser(UserDTO userDto){
        User user = modelMapper.map(userDto, User.class);
        User saved = userRepository.save(user);
        return modelMapper.map(saved, UserDTO.class); // return DTO, not entity
    }
}
