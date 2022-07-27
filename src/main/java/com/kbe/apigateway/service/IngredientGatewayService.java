package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.messageproducer.producer.CurrencyProducer;
import com.kbe.apigateway.messageproducer.producer.IngredientProducer;
import com.kbe.apigateway.messageproducer.producer.PriceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IngredientGatewayService {

    @Autowired
    private CurrencyProducer currencyProducer;
    @Autowired
    private IngredientConverterService ingredientConverterService;
    @Autowired
    private PriceAdjustmentService priceAdjustmentService;


    public String getIngredients(List<IngredientDTO> ingredients, String currency) throws JsonProcessingException {
        double rate = priceAdjustmentService.adjustCurrency(currency);
        String ingredientsJSONString;

        if(rate != 1.0) {
            List<IngredientDTO> adjustedIngredients = priceAdjustmentService.adjustIngredientsPrice(ingredients, rate);
            ingredientsJSONString = ingredientConverterService.mapIngredientListToJson(adjustedIngredients);
        }
        else ingredientsJSONString = ingredientConverterService.mapIngredientListToJson(ingredients);

        return ingredientsJSONString;
    }

    public String getIngredient(IngredientDTO ingredient, String currency) throws JsonProcessingException {
        double rate = priceAdjustmentService.adjustCurrency(currency);
        String ingredientJSONString;

        if(rate != 1.0) {
            IngredientDTO adjustedIngredient = priceAdjustmentService.adjustIngredientPrice(ingredient, rate);
            ingredientJSONString = ingredientConverterService.mapIngredientToJson(adjustedIngredient);
        }
        else ingredientJSONString = ingredientConverterService.mapIngredientToJson(ingredient);

        return ingredientJSONString;
    }

}