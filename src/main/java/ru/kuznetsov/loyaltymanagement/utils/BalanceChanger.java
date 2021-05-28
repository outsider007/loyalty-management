package ru.kuznetsov.loyaltymanagement.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceChanger {
    private BigInteger sum;
    private Integer operation;
}
