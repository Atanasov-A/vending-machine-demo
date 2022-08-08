package com.atanas.vendingmachinedemo.enums;

import java.math.BigDecimal;

public enum BulgarianCoins {
    TEN_COIN(new BigDecimal("0.10")), TWENTY_COIN(new BigDecimal("0.20")),
    FIFTY_COIN(new BigDecimal("0.50")), ONE_LEV_COIN(new BigDecimal("1.00")),
    TWO_LEV_COIN(new BigDecimal("2.00"));

    private BigDecimal coinValue;

    BulgarianCoins(BigDecimal coinValue) {
        this.coinValue = coinValue;
    }

    public BigDecimal getCoinValue() {
        return this.coinValue;
    }


}
