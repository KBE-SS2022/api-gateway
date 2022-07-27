package com.kbe.apigateway.service;

import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.messageproducer.producer.CurrencyProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceAdjustmentService {

    @Autowired
    private CurrencyProducer currencyProducer;

    public CompletePizzaDTO adjustPizzaPrice(CompletePizzaDTO pizza, double rate) {
        Double adjustedPrice = pizza.getPrice() * rate;
        pizza.setPrice(adjustedPrice);
        return pizza;
    }

    public IngredientDTO adjustIngredientPrice(IngredientDTO ingredient, double rate) {
        double adjustedPrice = ingredient.getPrice() * rate;
        ingredient.setPrice(adjustedPrice);
        return ingredient;
    }

    public List<CompletePizzaDTO> adjustPizzasPrice(List<CompletePizzaDTO> pizzaList, double rate) {
        return pizzaList.stream()
                .map(pizza -> adjustPizzaPrice(pizza, rate))
                .collect(Collectors.toList());
    }

    public List<IngredientDTO> adjustIngredientsPrice(List<IngredientDTO> indredientList, double rate) {
        return indredientList.stream()
                .map(ingredient -> adjustIngredientPrice(ingredient, rate))
                .collect(Collectors.toList());
    }

    public double adjustCurrency(String currency) {
        if(isCurrencyChanged(currency)) {
            String currencies = "EUR_" + currency;
            ResponseEntity<Double> receivedRate = currencyProducer.getCurrency(currencies);
            double rate = receivedRate.getBody();
            return rate;
        }
        return 1.0;
    }

    private boolean isCurrencyChanged(String currency) {
        return !currency.equals("EUR");
    }
}