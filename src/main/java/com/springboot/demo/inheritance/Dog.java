package com.springboot.demo.inheritance;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// Dog gets its OWN COMPLETE table
// dogs table has: id, name, sound (from Animal) + breed (own)
// All parent columns are DUPLICATED here — no join needed
@Entity
@Table(name = "dogs")
public class Dog extends Animal {
 
    private String breed;
    
}
