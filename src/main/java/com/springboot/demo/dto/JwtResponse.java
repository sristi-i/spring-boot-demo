package com.springboot.demo.dto;

import lombok.Data;
import java.util.List;

// what the server sends back after sucessful login
// so frontend can set up user session without another API call
@Data
public class JwtResponse {
    private String token; // the jwt string
    private String type = "Bearer"; // a;ways "Bearer" for JWT
    private String userName;
    private List<String> roles;

    public JwtResponse(String token, String userName, List<String> roles){
        this.token = token;
        this.userName = userName;
        this.roles = roles;
    }
}
