package com.springboot.demo.mapping;

// hibernate relationship mapping - owning side of all 3 mappings
// relationship 1 - @OneToOne with Address
// relationship 2 - @ManyToOne with Department
// relationship 1 - @ManyToMany with Project

// DB table - employees(id, name, email, address_id FK, dept_id FK)

import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data // generates ALL getters + setters + toString + equals + hashCode
@NoArgsConstructor // generates public Employee() {}
@Entity
@Table(name = "employees")
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // realtionshsip 1 - @OneToOne
    // a. @OneToOne + @JoinColumn creates address_id FK column 
    //      in employee table pointing to addresses(id)
    // b. cascade= ALL - saves/delete employee = save/delete address

    // unidirectional - only employee has reference to address - we use this
    //                  address doesnt not have employee reference 
    // bidireactional - both sides hold a reference to each ohter
    //                  Address would also have @oneOnOne(mappedBy="address")
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    // relationship 2 - @ManyToOne
    // a. @ManyToOne + @JoinColumn creates dept_id FK column
    //     in employee table pointing to department (id)
    
    // default fetch type for @ManyToOne - EAGER
    // department is loaded immediately in same query as employee
    // SELCT employees JOIN departments in one query

    // Deafult fetch types tble -
    // @OneToOne - EAGER
    // @ManyToOne - EAGER
    // @OneToMany - LAZY
    // @ManyToMany - LAZY

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dept_id", referencedColumnName = "id")
    private Department department;

    // realtionship 3 - @ManyToMany
    // a. @ManyToMany + @JoinTable
    //      Hibernate creates "wmployee_projects" join table with
    //      employee_id column (FK -> employees.id)
    //      project_id column (FK -> projects.id)

    // cascade PERSIST + MERGE only (not ALL) for @ManyToMany
    // if CASCADE = REMOVE = deleting employee would also delete the project
    // but project is shared by other employees too - never cascade remove in many to many relationships

    // @JoinTable - write to explicity to get control over table/column names
    // but hibernate auto- creates the jon table with a deafut name

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    @JoinTable(name = "employee_projects", joinColumns = @JoinColumn(name = "employee_id"), inverseJoinColumns = @JoinColumn(name = "project_id"))
    private List<Project> projects = new ArrayList<>();


}
