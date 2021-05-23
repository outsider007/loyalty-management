package ru.kuznetsov.loyaltymanagement.services.constants;

import java.util.HashMap;

public class CustomerContent {

    public static final HashMap<Integer, String> GENDER = new HashMap<>();

    static {
        GENDER.put(0, "Мужской");
        GENDER.put(1, "Женский");
    }
}
