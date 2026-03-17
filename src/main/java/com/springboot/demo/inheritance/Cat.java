package com.springboot.demo.inheritance;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// Cat gets its OWN COMPLETE table
// cats table has: id, name, sound (from Animal) + indoor (own)
@Entity
@Table(name = "cats")
public class Cat extends Animal {
 
    private boolean indoor;
 
    
}
