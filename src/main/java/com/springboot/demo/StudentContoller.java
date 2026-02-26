package com.springboot.demo;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentContoller {

    // in-memory list acting as our fake database
    private List<Student> students = new ArrayList<>(List.of(
        new Student(1, "Sristi", "sristi@email.com"),
        new Student(2, "Rahul", "rahul@email.com")
    ));

    // GET all students
    @GetMapping
    public List<Student> getAllStudents(){
        return students;
    }

    // GET student by id
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable int id){
        return students.stream()
                        .filter(s -> s.getId() == id)
                        .findFirst()
                        .orElse(null);
    }

    // POST - add new student
    @PostMapping
    public Student addStudent(@RequestBody Student student){
        students.add(student);
        return student;
    }

    // PUT - update student
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable int id, @RequestBody Student updated){
        for(Student s: students){
            if(s.getId() == id){
                s.setName(updated.getName());
                s.setEmail(updated.getEmail());
                return s;
            }
        }
        return null;
 
    }

    // DELETE - remove student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable int id){
        students.removeIf(s -> s.getId() == id);
        return "Student " + id + " deleted!";
    }


}