package com.atanas.vendingmachinedemo.unitTests.controllers;

import com.atanas.vendingmachinedemo.constants.ApiConstants;
import com.atanas.vendingmachinedemo.controllers.VendingMachineController;
import com.atanas.vendingmachinedemo.exceptions.NotEnoughMoneyException;
import com.atanas.vendingmachinedemo.exceptions.ProductNotAvailable;
import com.atanas.vendingmachinedemo.models.dto.ProductResponse;
import com.atanas.vendingmachinedemo.services.VendingMachineService;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@WebMvcTest(VendingMachineController.class)
public class VendingMachineControllerTest {

    @MockBean
    VendingMachineService vendingMachineService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testProductsLis() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                        ApiConstants.ENDPOINT_VENDING_MACHINE + ApiConstants.PATH_PRODUCTS))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProductByIdWrongIdFormat() throws Exception {
        String wrongIdFormat = "/123";
        mockMvc.perform(MockMvcRequestBuilders.get(
                        ApiConstants.ENDPOINT_VENDING_MACHINE + ApiConstants.PATH_PRODUCTS + wrongIdFormat))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInsertCoinNotExistingCoin() throws Exception {
        JSONObject json = new JSONObject();
        json.put("coin", "THIRTY_COIN");

        mockMvc.perform(MockMvcRequestBuilders.post(
                                ApiConstants.ENDPOINT_VENDING_MACHINE + ApiConstants.PATH_COINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testInsertCoin() throws Exception {
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("coin", "TWENTY_COIN");

        mockMvc.perform(MockMvcRequestBuilders.post(
                                ApiConstants.ENDPOINT_VENDING_MACHINE + ApiConstants.PATH_COINS)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonResult.toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void buyProduct() throws Exception {
        UUID productId = UUID.randomUUID();
        String productIdPath = "/" + productId;
        ProductResponse productResponse = new ProductResponse("monster");
        Mockito.when(vendingMachineService.buyProduct(productId)).thenReturn(productResponse);


        JSONObject jsonResult = new JSONObject();
        jsonResult.put("productName", "monster");
        jsonResult.put("productAmount", 1);

        mockMvc.perform(MockMvcRequestBuilders.get(
                        ApiConstants.ENDPOINT_VENDING_MACHINE + ApiConstants.PATH_PRODUCTS +
                                ApiConstants.PATH_BUY + "/" + productIdPath))
                .andExpect(status().isOk())
                .andExpect(content().json(jsonResult.toString()));
    }

    @Test
    public void buyProductNotEnoughMoney() throws Exception {
        UUID productId = UUID.randomUUID();
        String productIdPath = "/" + productId;
        Mockito.when(vendingMachineService.buyProduct(productId))
                .thenThrow(new NotEnoughMoneyException("Not enough money"));

        mockMvc.perform(MockMvcRequestBuilders.get(
                        ApiConstants.ENDPOINT_VENDING_MACHINE + ApiConstants.PATH_PRODUCTS +
                                ApiConstants.PATH_BUY + "/" + productIdPath))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void buyProductNotEnoughProductAmount() throws Exception {
        UUID productId = UUID.randomUUID();
        String productIdPath = "/" + productId;
        Mockito.when(vendingMachineService.buyProduct(productId))
                .thenThrow(new ProductNotAvailable("Product is sold out"));

        mockMvc.perform(MockMvcRequestBuilders.get(
                        ApiConstants.ENDPOINT_VENDING_MACHINE + ApiConstants.PATH_PRODUCTS +
                                ApiConstants.PATH_BUY + "/" + productIdPath))
                .andExpect(status().isNotFound());
    }
}

