package com.springboot.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity, Long>{
    // No code needed! JpaRepositry gives us:
    // save(), findAll(), findBy(), deleteBy() automatically!
    
}