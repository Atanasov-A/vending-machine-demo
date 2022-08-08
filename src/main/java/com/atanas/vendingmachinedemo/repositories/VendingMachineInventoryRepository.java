package com.atanas.vendingmachinedemo.repositories;

import com.atanas.vendingmachinedemo.enums.BulgarianCoins;
import com.atanas.vendingmachinedemo.models.Product;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Repository
public class VendingMachineInventoryRepository {

    private List<BulgarianCoins> insertedCoins = new ArrayList<>();

    private ArrayList<Product> products = new ArrayList<>();

    public VendingMachineInventoryRepository() {
        this.fillWithProducts();
    }

    public List<BulgarianCoins> getInsertedCoins() {
        return insertedCoins;
    }

    public void setInsertedCoins(List<BulgarianCoins> insertedCoins) {
        this.insertedCoins = insertedCoins;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Product findProductById(UUID productId) throws NoSuchElementException {
        System.out.println("Debug: Find product by id - [" + productId + "]");
        Product product = this.products.stream().filter(p -> p.getId().equals(productId)).findFirst().orElseThrow();
        return product;
    }

    public boolean deleteProduct(UUID productId) {
        return this.products.removeIf(product -> product.getId().equals(productId));
    }

    private void fillWithProducts() {
        Product cola = new Product("coca-cola", new BigDecimal("1.20"), 10);
        Product monster = new Product("monster", new BigDecimal("1.20"), 10);
        Product sandwich = new Product("sandwich", new BigDecimal("1.20"), 10);
        Product kitkat = new Product("kitkat", new BigDecimal("1.15"), 10);
        this.products.add(cola);
        this.products.add(monster);
        this.products.add(sandwich);
        this.products.add(kitkat);
    }
}
