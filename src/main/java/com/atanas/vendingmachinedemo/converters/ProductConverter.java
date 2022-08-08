package com.atanas.vendingmachinedemo.converters;

import com.atanas.vendingmachinedemo.models.Product;
import com.atanas.vendingmachinedemo.models.dto.ProductDto;

public class ProductConverter {

    public static Product convertProductDtoToProduct(ProductDto productDto) {
        return new Product(
                productDto.getProductName(),
                productDto.getProductPrice(),
                productDto.getProductAmount());
    }
}
