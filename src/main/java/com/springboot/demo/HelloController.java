package com.springboot.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController{
    @GetMapping("/hello")
    public String hello(){
        return "Hello world from Spring Boot";
    
    }

    @GetMapping("/")
    public String home(){
        return "Spring Boot App is running!";
    }


}