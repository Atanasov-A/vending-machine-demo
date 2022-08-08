package com.atanas.vendingmachinedemo.models.dto;

public class ProductResponse {

    private String productName;
    private int productAmount = 1;

    public ProductResponse(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductAmount() {
        return productAmount;
    }

}
