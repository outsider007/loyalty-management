package ru.kuznetsov.loyaltymanagement.utils;

import com.google.common.collect.ImmutableMap;

public class OperationType {
    public static final Integer SUM = 0;
    public static final Integer DIFFERENCE = 1;

    public static final ImmutableMap<Integer, String> OPERATION_TYPES = ImmutableMap.of(SUM, "Зачисление", DIFFERENCE, "Списание");
}
