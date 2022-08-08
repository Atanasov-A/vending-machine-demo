package com.atanas.vendingmachinedemo.utils;

import java.util.UUID;

public class Helpers {

    public static UUID convertStringToUuid(String id) throws IllegalArgumentException {
        try {
            UUID uuid = UUID.fromString(id);
            return uuid;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Id is not uuid");
        }
    }
}
