package com.springboot.demo.inheritance;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
// Bike gets its OWN table with only bike-specific columns
@Entity
@Table(name = "bikes")
@PrimaryKeyJoinColumn(name = "id")
public class Bike extends Vehicle {
 
    @Column(name = "bike_type")
    private String bikeType; // SPORTS, CRUISER, ELECTRIC
}
