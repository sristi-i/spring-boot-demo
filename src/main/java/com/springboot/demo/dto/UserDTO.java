
package com.springboot.demo.dto;  

import lombok.Data;

// Why seperate DTO from Entity?
// Entity has ALL fields including password, internal IDs
// DTO exposes ONLY what API consumer should see
@Data
public class UserDTO{

    private Long id;
    private String name;
    private String username;

    // No password field - security! never expose password in response

}