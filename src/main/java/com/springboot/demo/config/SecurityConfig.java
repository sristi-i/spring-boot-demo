package com.springboot.demo.config;

import com.springboot.demo.filter.JwtAuthenticationFilter;
import com.springboot.demo.service.UserService;

import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration 
@EnableWebSecurity // activates Spring security for this application
public class SecurityConfig {
    
    @Autowired private JwtAuthenticationFilter jwtAuthenticationFilter;
    @Autowired private UserService userService;
    @Autowired private PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity httpSecurity) throws Exception{
        // JWT uses Authorization header - browser cant auto-send it
        // so CSRF attacks dont apply -> safe to disable for REST APIs

        httpSecurity.csrf(csrf -> csrf.disable())
                    .authorizeHttpRequests(auth -> auth
                                            .requestMatchers("/api/auth/**").permitAll() // login public
                                            .requestMatchers("/api/users/register").permitAll() // register public
                                            .anyRequest().authenticated() // everything else needs JWT
                    )
                    .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authenticationProvider(authenticationProvider())
                    .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

     // DaoAuthenticationProvider = wires UserDetailsService + PasswordEncoder
    // this is what actually verifies credentials during login
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider p = 
            new DaoAuthenticationProvider(userService); 
        p.setPasswordEncoder(passwordEncoder);        
        return p;
    }

    // Spring doesn't expose AuthenticationManager as a bean automatically
    // must declare it so AuthController can @Autowired and use it
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
