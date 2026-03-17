package com.springboot.demo.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/mapping")
public class MappingController {
    
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // a. @OneToOne + @ManyToOne
    //      save meployee with address + department
    //      address auto saved via cascade
    @PostMapping("/employee")
    public Employee saveEmployee(@RequestBody Employee employee)
    {
        return employeeRepository.save(employee);
    }

    @GetMapping("/employee")
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    @GetMapping("/employee/{id}")
    public Employee getEmployeeById(@PathVariable Long id){
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found: " + id));
    }

    @DeleteMapping("/employee/{id}")
    public String deleteEmployee(@PathVariable Long id){
        employeeRepository.deleteById(id);
        return "Employee deleted - address also deleted";
    }

    // b. @OneToMany
    //      save department with multiple empoyees
    //      employees auto-saved via cascade = ALL
    @PostMapping("/department")
    public Department saveDepartment(@RequestBody Department department){
        // sync both sides of bidiractional relationshiop
        if(department.getEmployees() != null){
            department.getEmployees().forEach(emp -> emp.setDepartment(department));
        }
        return departmentRepository.save(department);
    }

    @GetMapping("/department")
    public List<Department> getAllDepartment(){
        return departmentRepository.findAll();
    }

    // c. @ManyToMany
    // save project - employee-project link done via employee POST
    @PostMapping("/project")
    public Project saveProject(@RequestBody Project project){
        return projectRepository.save(project);
    }

    @GetMapping("/project")
    public List<Project> getAllProjects(){
        return projectRepository.findAll();
    }

    
}
