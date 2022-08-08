package com.atanas.vendingmachinedemo.exceptions;


public class ProductNotAvailable extends Exception {

    public ProductNotAvailable(String errorMessage) {
        super(errorMessage);
    }

}

