package com.springboot.demo.inheritance;

// HIBERNATE INHERITANCE STRATEGY 2: JOINED
// SCENARIO: Vehicle → Car, Bike

// When Hibernate saves a Car:
//           INSERT into vehicles (brand, price) → gets id = 1
//           INSERT into cars     (id=1, num_doors=4)
//           When Hibernate reads a Car:
//           SELECT v.*, c.* FROM vehicles v JOIN cars c ON v.id = c.id
//
// DB TABLES CREATED (ONE per class) 
//   vehicles   → id, brand, price
//   cars       → id (FK to vehicles), num_doors
//   bikes      → id (FK to vehicles), bike_type
//
//   To get a full Car row, Hibernate JOINs vehicles + cars:
//   vehicles: id=1, brand="Toyota", price=800000
//   cars:     id=1, num_doors=4
//   Result:   {id=1, brand="Toyota", price=800000, num_doors=4}

import jakarta.persistence.*;
import lombok.Data;
 
@Data
@Entity
@Table(name = "vehicles")
 
// JOINED strategy — one table per class in hierarchy
@Inheritance(strategy = InheritanceType.JOINED)
public class Vehicle {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String brand;
    private Double price;
 
    
}
