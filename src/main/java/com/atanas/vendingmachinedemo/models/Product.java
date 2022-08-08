package com.atanas.vendingmachinedemo.models;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * For advanced use cases Factory pattern can be implemented.
 */
public class Product {

    private UUID id;
    private String productName;
    private BigDecimal productPrice;
    private int productAmount;

    public Product(String productName, BigDecimal productPrice, int productAmount) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.id = UUID.randomUUID();
        // TODO fix
        if (productAmount < 0) {
            System.out.println("Error: product amount can't be less than 0");
        }
        this.productAmount = productAmount;
    }

    public UUID getId() {
        return id;
    }

    public int getProductAmount() {
        return productAmount;
    }

    public String getProductName() {
        return productName;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public void setProductAmount(int productAmount) {
        this.productAmount = productAmount;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", productAmount=" + productAmount +
                '}';
    }
}
