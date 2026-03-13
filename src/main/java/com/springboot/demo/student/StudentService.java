package com.springboot.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<StudentEntity> getAllStudents(){
        return studentRepository.findAll();
    }

    public void saveStudent(StudentEntity student){
        studentRepository.save(student);
    }

    public StudentEntity getStudentById(Long id){
        return studentRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Student not found")
        );
    }

    public void deleteStudent(Long id){
        studentRepository.deleteById(id);
    }

}