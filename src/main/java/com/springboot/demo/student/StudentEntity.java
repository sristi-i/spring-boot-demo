package com.springboot.demo.student;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

@Entity
@Table(name="Students")
public class StudentEntity{
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email")
    private String email;

    public StudentEntity(){}

    public StudentEntity(String firstName, String lastName, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // getters ans setters
    public Long getId(){return id;}
    public void setId(Long id) {this.id = id;}

    public String getFirstName(){return firstName;}
    public void setFirstName(String firstName){ this.firstName = firstName;}

    public String getLastName(){return lastName;}
    public void setLastName(String lastName){ this.lastName = lastName;}

    public String getEmail(){return email;}
    public void setEmail(String email){ this.email = email;}

}