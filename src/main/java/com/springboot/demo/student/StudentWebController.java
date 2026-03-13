package com.springboot.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/web/students")
public class StudentWebController {
    
    @Autowired
    private StudentService studentService;

    // list all students
    @GetMapping
    public String listStudents(Model model){
        model.addAttribute("students", studentService.getAllStudents());
        return "students/list";
    }

    // show add form
    @GetMapping("/new")
    public String showAddForm(Model model){
        model.addAttribute("student", new StudentEntity());
        return "students/form";
    }

    // save new or updated student
    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") StudentEntity student,
                            BindingResult result) {
        if (result.hasErrors()) {
            return "students/form";
        }
        studentService.saveStudent(student);
        return "redirect:/web/students";
    }

    // show edit form
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model){
        model.addAttribute("student", studentService.getStudentById(id));
        return "students/form";
    }

    // delete student
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return "redirect:/web/students";
    }

}