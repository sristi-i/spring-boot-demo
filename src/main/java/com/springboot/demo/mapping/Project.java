package com.springboot.demo.mapping;

// @ManyToMany mapping (inverse side)
// one employee works on many projects
// one project has many employees

// projects (id, project_name, client)
// employees (employee_id, project_id)

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectName;
    private String client;

    // inverse side - just mappedBy, no @JoinTable here
    // mappedBy = "projects" -> referes to field name in Employee.java
    @JsonProperty(access = JsonProperty.Access.READ_ONLY) 
    @ManyToMany(mappedBy = "projects", fetch = FetchType.LAZY)
    private List<Employee> employees = new ArrayList<>();

    
}
