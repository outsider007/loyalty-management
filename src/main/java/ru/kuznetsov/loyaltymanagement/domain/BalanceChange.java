package ru.kuznetsov.loyaltymanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "balance_history_change")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "balance_id")
    private String balanceId;
    @Column(name = "operation_type_id")
    private Integer operationTypeId;
    @Column(name = "sum")
    private BigInteger sum;
    @Column(name = "date_change")
    private Date dateChange;
}
