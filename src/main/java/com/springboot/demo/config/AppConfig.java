package com.springboot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.modelmapper.ModelMapper;

@Configuration
public class AppConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // why @Bean? - so Spring manages one shared instance (Singleton)
    // ModelMapper is thread-safe, so one instance for the whole app is fine
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}