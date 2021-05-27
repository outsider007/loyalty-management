package ru.kuznetsov.loyaltymanagement.rest.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BalanceChanger {
    private BigInteger sumChange;
    private Integer operationType;
}
