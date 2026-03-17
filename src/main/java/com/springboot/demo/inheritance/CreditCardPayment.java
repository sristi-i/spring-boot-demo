package com.springboot.demo.inheritance;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("CREDIT")
public class CreditCardPayment extends Payment {
 
    @Column(name = "card_number")
    private String cardNumber;
 
    @Column(name = "card_holder")
    private String cardHolder;

}
