package com.atanas.vendingmachinedemo.services.impl;

import com.atanas.vendingmachinedemo.converters.ProductConverter;
import com.atanas.vendingmachinedemo.enums.BulgarianCoins;
import com.atanas.vendingmachinedemo.exceptions.NotEnoughMoneyException;
import com.atanas.vendingmachinedemo.exceptions.ProductNotAvailable;
import com.atanas.vendingmachinedemo.models.Product;
import com.atanas.vendingmachinedemo.models.dto.ProductDto;
import com.atanas.vendingmachinedemo.models.dto.ProductResponse;
import com.atanas.vendingmachinedemo.repositories.VendingMachineInventoryRepository;
import com.atanas.vendingmachinedemo.services.VendingMachineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class VendingMachineServiceImpl implements VendingMachineService {

    @Autowired
    private VendingMachineInventoryRepository vendingMachineInventoryRepository;

    @Override
    public List<Product> getAllProductsInTheMachine() {
        return vendingMachineInventoryRepository.getProducts();
    }

    @Override
    public Product getProductById(UUID productId) throws NoSuchElementException {
        try {
            Product product = vendingMachineInventoryRepository.findProductById(productId);
            return product;
        } catch (NoSuchElementException e) {
            System.out.println("Debug: No element with the id [" + productId.toString() + "] found");
            throw new NoSuchElementException("Product with this id is not found");
        }
    }

    @Override
    public Product addProduct(ProductDto productDto) {
        Product product = ProductConverter.convertProductDtoToProduct(productDto);
        System.out.println("Debug: added product with the id [" + product.getId() + "]");
        vendingMachineInventoryRepository.addProduct(product);
        return product;
    }

    @Override
    public UUID deleteProduct(UUID productId) throws NoSuchElementException {
        boolean isRemoved = vendingMachineInventoryRepository.deleteProduct(productId);
        if (isRemoved) {
            System.out.println("Debug: Deleted product with the id [" + productId.toString() + "]");
        } else {
            System.out.println("Debug: No element with the id [" + productId.toString() + "] found");
            throw new NoSuchElementException("Product with this id is not found");
        }
        return productId;
    }

    @Override
    public Product updateProduct(ProductDto productDto, UUID productId) throws NoSuchElementException {
        try {
            Product product = vendingMachineInventoryRepository.findProductById(productId);
            System.out.println("Debug: Product with the id [" + product.getId() + "] updated.");

            product.setProductName(productDto.getProductName());
            product.setProductAmount(productDto.getProductAmount());
            product.setProductPrice(productDto.getProductPrice());

            return product;

        } catch (NoSuchElementException e) {
            System.out.println("Debug: No element with the id [" + productId.toString() + "] found");
            throw new NoSuchElementException("Product with this id is not found");
        }
    }

    @Override
    public void insertCoin(BulgarianCoins bulgarianCoin) {
        vendingMachineInventoryRepository.getInsertedCoins().add(bulgarianCoin);
        System.out.println("Debug: Inserted a coin with the value [" + bulgarianCoin.getCoinValue().toString() + "]");
    }

    @Override
    public List<BulgarianCoins> showInsertedCoins() {
        System.out.println("Debug: Inserted a coins size [" +
                this.vendingMachineInventoryRepository.getInsertedCoins().size() + "]");
        return this.vendingMachineInventoryRepository.getInsertedCoins();
    }

    @Override
    public List<BulgarianCoins> returnInsertedCoins() {
        List<BulgarianCoins> coinsInTheMachine = List.copyOf(this.vendingMachineInventoryRepository.getInsertedCoins());
        System.out.println("Debug: Returned [" + coinsInTheMachine.size() + "] coins");
        this.vendingMachineInventoryRepository.getInsertedCoins().clear();
        return coinsInTheMachine;
    }

    @Override
    public ProductResponse buyProduct(UUID productId) throws NoSuchElementException,
            NotEnoughMoneyException, ProductNotAvailable {
        try {
            Product product = vendingMachineInventoryRepository.findProductById(productId);
            System.out.println("Debug: Buy product with id: " + productId);
            BigDecimal insertedCoinsSum = vendingMachineInventoryRepository.getInsertedCoins()
                    .stream()
                    .map(bulgarianCoins -> bulgarianCoins.getCoinValue())
                    .reduce(new BigDecimal("0.00").setScale(2), (subSum, currentEl) -> subSum.add(currentEl));

            System.out.println("Debug: Inserted coins sum: " + insertedCoinsSum);

            int productCurrentAmount = product.getProductAmount();

            System.out.println("Debug: Product amount: " + productCurrentAmount);

            if (insertedCoinsSum.compareTo(product.getProductPrice()) > 0) {
                if (productCurrentAmount > 0) {
                    int productNewAmount = productCurrentAmount - 1;
                    product.setProductAmount(productNewAmount);
                } else {
                    throw new ProductNotAvailable("Product is sold out.");
                }
            } else {
                throw new NotEnoughMoneyException("Not enough money in the machine to buy the product. " +
                        "Current amount: " + insertedCoinsSum.toString());
            }

            System.out.println("Debug: Product name [" + product.getId() + " ] and amount after the selling: "
                    + productCurrentAmount);

            this.vendingMachineInventoryRepository.getInsertedCoins().clear();

            return new ProductResponse(product.getProductName());
        } catch (NoSuchElementException e) {
            System.out.println("Debug: No element with the id [" + productId.toString() + "] found");
            throw new NoSuchElementException("Product with this id is not found");
        }
    }
}
