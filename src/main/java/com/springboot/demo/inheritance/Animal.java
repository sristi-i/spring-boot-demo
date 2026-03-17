package com.springboot.demo.inheritance;

// HIBERNATE INHERITANCE STRATEGY 3: TABLE_PER_CLASS
// SCENARIO: Animal → Dog, Cat


// Use GenerationType.AUTO (NOT IDENTITY) for the ID.
//           INTERVIEW: Why not IDENTITY for TABLE_PER_CLASS?
//           IDENTITY generates: dogs id starts at 1, cats id starts at 1.
//           → Both tables have id=1 → conflict in polymorphic queries.
//           → AUTO uses a shared sequence → ids are unique across ALL tables.
//           dogs: id=1  cats: id=2  (never both id=1)
//
// DB TABLES CREATED (one complete table per concrete class) 
//   dogs   → id, name, sound, breed     (all columns including parent's)
//   cats   → id, name, sound, indoor    (all columns including parent's)
//   Animal table: NOT CREATED (abstract class)


import jakarta.persistence.*;
 
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
// TABLE_PER_CLASS strategy
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
// No @Table here — abstract parent has no own table
public abstract class Animal {
 
    // STEP 5: GenerationType.AUTO — uses shared sequence
    // NOT IDENTITY — would cause duplicate IDs across tables
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
 
    // These fields get DUPLICATED into both dogs and cats tables
    private String name;
    private String sound;
    
}
