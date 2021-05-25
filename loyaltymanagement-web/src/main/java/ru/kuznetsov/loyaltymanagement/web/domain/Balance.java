package ru.kuznetsov.loyaltymanagement.web.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "customer_id")
    private Integer customerId;
    @Column(name = "balance")
    private BigInteger balance;
}
