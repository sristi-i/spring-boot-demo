package com.springboot.demo.inheritance;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

// Car gets its OWN table with only car-specific columns
@Data
@NoArgsConstructor
@Entity
@Table(name = "cars")
@PrimaryKeyJoinColumn(name = "id")
public class Car extends Vehicle {
 
    @Column(name = "num_doors")
    private int numberOfDoors;
    
}
