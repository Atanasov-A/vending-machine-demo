package com.atanas.vendingmachinedemo.validators;

import com.atanas.vendingmachinedemo.models.dto.ProductDto;

import java.math.BigDecimal;

public class NewProductValidator {

    public static boolean isNewProductValid(ProductDto productDto) throws IllegalArgumentException {
        if (productDto.getProductName().isEmpty() || productDto.getProductName().isBlank() ||
                productDto.getProductName() == null) {
            throw new IllegalArgumentException("Incorrect product name");
        }
        if (productDto.getProductAmount() <= 0 || productDto.getProductAmount() > 10) {
            throw new IllegalArgumentException("Product amount must be greater than 0 and less than 10");
        }
        if (productDto.getProductPrice().compareTo(new BigDecimal("0.00")) <= 0) {
            throw new IllegalArgumentException("Product price can't be null or negative");
        }
        return true;
    }
}
