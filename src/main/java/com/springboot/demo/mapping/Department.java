package com.springboot.demo.mapping;

// @OneToMany mapping - parent/inverse side
// one department has many employees

// departments (id, dept_name, location)
// employees (id, name, email, dept_id)

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deptName;
    private String location;

    // relationship @OneToMany - on inverse (parent) side
    // mappedBy = "department" -> refers to field name in Employee.java
    // cascade = ALL
    // fetch = LAZY
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

}
