package com.atanas.vendingmachinedemo.exceptions;

public class NotEnoughMoneyException extends Exception {

    public NotEnoughMoneyException(String errorMessage) {
        super(errorMessage);
    }

}
