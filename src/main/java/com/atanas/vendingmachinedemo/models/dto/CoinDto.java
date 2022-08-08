package com.atanas.vendingmachinedemo.models.dto;

import com.atanas.vendingmachinedemo.enums.BulgarianCoins;

public class CoinDto {

    private BulgarianCoins coin;

    public BulgarianCoins getCoin() {
        return coin;
    }

    public void setCoin(BulgarianCoins coin) {
        this.coin = coin;
    }
}
