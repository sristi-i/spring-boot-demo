package com.springboot.demo.inheritance;

// HIBERNATE INHERITANCE STRATEGY 1: SINGLE_TABLE
// SCENARIO: Payment → CreditCardPayment, CashPayment

import jakarta.persistence.*;
 
@Entity
@Table(name = "payments")
 
// Declare SINGLE_TABLE strategy on parent
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//   A special column Hibernate uses to identify which subclass a row is.
//   Default name is "DTYPE" if you don't specify @DiscriminatorColumn.
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)

// Value stored in "type" column for Payment rows
@DiscriminatorValue("PAYMENT")
public class Payment {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private Double amount;
    private String status; // SUCCESS, FAILED, PENDING
 
    public Payment() {}
    public Payment(Double amount, String status) {
        this.amount = amount; this.status = status;
    }
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
 
