package com.springboot.demo.inheritance;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@DiscriminatorValue("CASH")
public class CashPayment extends Payment {
 
    @Column(name = "bank_name")
    private String bankName;
}
