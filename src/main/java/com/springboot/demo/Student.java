package com.springboot.demo;

public class Student {

    // no-arg constructor needed for JSON desearalization
    public Student(){}

    private Integer id;
    private String name;
    private String email;

    // Constructor
    public Student(Integer id, String name, String email){
        this.id  = id;
        this.name = name;
        this.email = email;
    }

    // getters and setters
    public Integer getId(){return id;}
    public void setId(Integer id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}
    
}