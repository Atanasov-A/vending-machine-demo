package com.atanas.vendingmachinedemo.services;


import com.atanas.vendingmachinedemo.enums.BulgarianCoins;
import com.atanas.vendingmachinedemo.exceptions.NotEnoughMoneyException;
import com.atanas.vendingmachinedemo.exceptions.ProductNotAvailable;
import com.atanas.vendingmachinedemo.models.Product;
import com.atanas.vendingmachinedemo.models.dto.ProductDto;
import com.atanas.vendingmachinedemo.models.dto.ProductResponse;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface VendingMachineService {

    List<BulgarianCoins> showInsertedCoins();

    void insertCoin(BulgarianCoins bulgarianCoin);

    List<BulgarianCoins> returnInsertedCoins();

    List<Product> getAllProductsInTheMachine();

    Product getProductById(UUID productId) throws NoSuchElementException;

    Product addProduct(ProductDto productDto);

    Product updateProduct(ProductDto productDto, UUID productId) throws NoSuchElementException;

    UUID deleteProduct(UUID productId) throws NoSuchElementException;

    ProductResponse buyProduct(UUID productId) throws NoSuchElementException, NotEnoughMoneyException,
            ProductNotAvailable;


}
