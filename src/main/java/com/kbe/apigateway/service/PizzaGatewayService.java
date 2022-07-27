package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import com.kbe.apigateway.messageproducer.producer.CurrencyProducer;
import com.kbe.apigateway.messageproducer.producer.IngredientProducer;
import com.kbe.apigateway.messageproducer.producer.PriceProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Service
public class PizzaGatewayService {

    @Autowired
    private CurrencyProducer currencyProducer;
    @Autowired
    private PriceProducer priceProducer;
    @Autowired
    private PizzaConverterService pizzaConverterService;
    @Autowired
    private PriceAdjustmentService priceAdjustmentService;
    @Autowired
    private PizzaCompleterService pizzaCompleterService;

    public String getPizzas(List<PizzaDTO> pizzas, List<IngredientDTO> ingredients, String currency) throws JsonProcessingException {
        List<CompletePizzaDTO> pizzasWithIngredients = pizzaCompleterService.completeIngredientsForPizzas(
                pizzas, ingredients);
        Map<Long,Double> pizzaPrices = this.getPrices(pizzas);
        List<CompletePizzaDTO> completePizzas = pizzaCompleterService.addPricesToPizzas(pizzasWithIngredients, pizzaPrices);
        double rate = priceAdjustmentService.adjustCurrency(currency);
        String pizzasJSONString;

        if(rate != 1.0) {
            List<CompletePizzaDTO> adjustedPizzas = priceAdjustmentService.adjustPizzasPrice(completePizzas, rate);
            pizzasJSONString = pizzaConverterService.mapCompletePizzaDTOListToJson(adjustedPizzas);
        }
        else pizzasJSONString = pizzaConverterService.mapCompletePizzaDTOListToJson(completePizzas);

        return pizzasJSONString;
    }

    public String getPizza(PizzaDTO pizza, List<IngredientDTO> ingredients, String currency) throws JsonProcessingException {
        CompletePizzaDTO pizzaWithIngredients = pizzaCompleterService.completeIngredients(pizza, ingredients);
        Map<Long,Double> pizzaPrice = this.getPrices(List.of(pizza));
        CompletePizzaDTO completePizzas = pizzaCompleterService.addPriceToPizza(pizzaWithIngredients, pizzaPrice);
        double rate = priceAdjustmentService.adjustCurrency(currency);
        String pizzaJSONString;

        if(rate != 1.0) {
            CompletePizzaDTO adjustedPizza = priceAdjustmentService.adjustPizzaPrice(completePizzas, rate);
            pizzaJSONString = pizzaConverterService.mapCompletePizzaDTOToJson(adjustedPizza);
        }
        else pizzaJSONString = pizzaConverterService.mapCompletePizzaDTOToJson(completePizzas);

        return pizzaJSONString;
    }

    private Map<Long, Double> getPrices(List<PizzaDTO> pizzas) {
        Map<Long, Double> idPriceMapForPizzas = new HashMap<>();
        for (PizzaDTO pizza : pizzas) {
            ResponseEntity<Map<Long,Double>> receivedPrice = priceProducer.getPrice(pizza);
            Map<Long,Double> priceMap = receivedPrice.getBody();
            idPriceMapForPizzas.putAll(priceMap);
        }
        return idPriceMapForPizzas;
    }
}