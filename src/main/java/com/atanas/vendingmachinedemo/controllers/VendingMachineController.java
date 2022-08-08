package com.atanas.vendingmachinedemo.controllers;

import com.atanas.vendingmachinedemo.constants.ApiConstants;
import com.atanas.vendingmachinedemo.enums.BulgarianCoins;
import com.atanas.vendingmachinedemo.exceptions.NotEnoughMoneyException;
import com.atanas.vendingmachinedemo.exceptions.ProductNotAvailable;
import com.atanas.vendingmachinedemo.models.Product;
import com.atanas.vendingmachinedemo.models.dto.CoinDto;
import com.atanas.vendingmachinedemo.models.dto.ProductDto;
import com.atanas.vendingmachinedemo.models.dto.ProductResponse;
import com.atanas.vendingmachinedemo.services.VendingMachineService;
import com.atanas.vendingmachinedemo.utils.Helpers;
import com.atanas.vendingmachinedemo.validators.NewProductValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Tag(
        name = "Vending Machine",
        description = "List of the endpoints allowing interaction with the vending machine."
)
@RestController
@RequestMapping(ApiConstants.ENDPOINT_VENDING_MACHINE)
public class VendingMachineController {

    @Autowired
    private VendingMachineService vendingMachineService;

    @Operation(summary = "This method is used for listing all products from the vending machine.")
    @GetMapping(value = ApiConstants.PATH_PRODUCTS)
    ResponseEntity<List<Product>> getAllProducts() {
        System.out.println("Debug: getAllProducts");
        return new ResponseEntity<>(vendingMachineService.getAllProductsInTheMachine(), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used for listing a product by its id.",
            description = "Get information about a product using a valid UUID. Check the response codes for more " +
                    "information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Product doesn't exist", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Wrong uuid format", content = @Content())

    })
    @GetMapping(ApiConstants.PATH_PRODUCTS + "/{" + ApiConstants.PATH_VARIABLE_ID + "}")
    ResponseEntity<Product> getProductById(@PathVariable(ApiConstants.PATH_VARIABLE_ID) String productId) {
        try {
            System.out.println("Debug: getProductById: [" + productId + "]");
            UUID id = Helpers.convertStringToUuid(productId);
            Product product = vendingMachineService.getProductById(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id {" + productId + "}  not found");
        }
    }

    @Operation(
            summary = "This method is used for deleting a product by its id.",
            description = "Delete a product by UUID. Check the response codes for more information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Product doesn't exist", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Wrong uuid format", content = @Content())

    })
    @DeleteMapping(ApiConstants.PATH_PRODUCTS + "/{" + ApiConstants.PATH_VARIABLE_ID + "}")
    ResponseEntity<String> deleteProductById(@PathVariable(ApiConstants.PATH_VARIABLE_ID) String productId) {
        try {
            System.out.println("Debug: delete product by id [" + productId + "]");
            UUID id = Helpers.convertStringToUuid(productId);

            String uuid = vendingMachineService.deleteProduct(id).toString();
            return new ResponseEntity<>(uuid, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product with id {" + productId + "}  not found");
        }
    }

    @Operation(
            summary = "This method is used for updating a product by its id.",
            description = "Update a product by UUID. They are some limitations, that can lead to a bad request. " +
                    "For example: the price of the product must be positive and not null and the amount of the product " +
                    "must be in the range 0 to 10. Check the response codes for more information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Product doesn't exist", content = @Content()),
            @ApiResponse(responseCode = "400", description = "Wrong uuid format", content = @Content())
    })
    @PutMapping(
            value = ApiConstants.PATH_PRODUCTS + "/{" + ApiConstants.PATH_VARIABLE_ID + "}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Product> updateProduct(@RequestBody ProductDto productDto,
                                          @PathVariable(ApiConstants.PATH_VARIABLE_ID) String productId) {
        try {
            System.out.println("Debug: update product with id [" + productId + "]");
            UUID id = Helpers.convertStringToUuid(productId);
            boolean isValid = NewProductValidator.isNewProductValid(productDto);

            Product updatedProduct = vendingMachineService.updateProduct(productDto, id);
            return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @Operation(
            summary = "This method is used for creating a product by its id.",
            description = "They are some limitations, that can lead to a bad request. " +
                    "For example: the price of the product must be positive and not null and the amount of the product " +
                    "must be in the range 0 to 10. Check the response codes for more information.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200"),
            @ApiResponse(responseCode = "404", description = "Product doesn't exist", content = @Content())
    })
    @PostMapping(
            value = ApiConstants.PATH_PRODUCTS,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<Product> addProduct(@RequestBody ProductDto productDto) {
        System.out.println("Debug: Add product: " + productDto.toString());
        try {
            boolean isValid = NewProductValidator.isNewProductValid(productDto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
        Product newProduct = vendingMachineService.addProduct(productDto);

        return new ResponseEntity<>(newProduct, HttpStatus.OK);
    }

    @Operation(summary = "This method is used for listing all inserted coins in the vending machine.")
    @GetMapping(ApiConstants.PATH_COINS)
    ResponseEntity<List<BulgarianCoins>> getInsertedCoins() {
        System.out.println("Debug: Get inserted coins");
        return new ResponseEntity<>(vendingMachineService.showInsertedCoins(), HttpStatus.OK);
    }

    @Operation(summary = "This method is used for inserting a coin in the vending machine.")
    @PostMapping(
            value = ApiConstants.PATH_COINS,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<String> insertCoins(@RequestBody CoinDto coin) {
        System.out.println("Debug: Insert coin: " + coin.getCoin().toString());
        vendingMachineService.insertCoin(coin.getCoin());
        return new ResponseEntity<>("Inserted coin with the value of " + coin.getCoin().getCoinValue(),
                HttpStatus.OK);
    }

    @Operation(summary = "This method is used for getting the inserted coins back from the vending machine.")
    @DeleteMapping(
            ApiConstants.PATH_COINS
    )
    ResponseEntity<List<BulgarianCoins>> getCoinsBack() {
        System.out.println("Debug: Get coins back");
        return new ResponseEntity<>(vendingMachineService.returnInsertedCoins(), HttpStatus.OK);
    }

    @Operation(
            summary = "This method is used for buying a product by its id.",
            description = "In order to buy a product, first, coins must be inserted in the machine. " +
                    "Get inserted coins endpoint can be used to check the inserted coins in the machine. ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The product was bought successful"),
            @ApiResponse(responseCode = "404", description = "Product doesn't exist or it's sold out",
                    content = @Content()),
            @ApiResponse(responseCode = "400", description = "Wrong uuid format", content = @Content())
    })
    @GetMapping(ApiConstants.PATH_PRODUCTS + ApiConstants.PATH_BUY + "/{" + ApiConstants.PATH_VARIABLE_ID + "}")
    ResponseEntity<ProductResponse> buyProduct(@PathVariable(ApiConstants.PATH_VARIABLE_ID) String productId) {
        try {
            System.out.println("Debug: update product with id [" + productId + "]");
            UUID id = Helpers.convertStringToUuid(productId);
            ProductResponse product = vendingMachineService.buyProduct(id);
            return new ResponseEntity<>(product, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ProductNotAvailable e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (NotEnoughMoneyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
