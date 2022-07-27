package com.kbe.apigateway.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import com.kbe.apigateway.messageproducer.producer.CurrencyProducer;
import com.kbe.apigateway.messageproducer.producer.IngredientProducer;
import com.kbe.apigateway.messageproducer.producer.PizzaProducer;
import com.kbe.apigateway.messageproducer.producer.PriceProducer;
import com.kbe.apigateway.service.IngredientConverterService;
import com.kbe.apigateway.service.PizzaCompleterService;
import com.kbe.apigateway.service.PizzaConverterService;
import com.kbe.apigateway.service.PriceAdjustmentService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.*;


@RestController
@CrossOrigin
@RequestMapping
public class ApiGatewayController {

    @Autowired
    IngredientConverterService ingredientConverterService;
    @Autowired
    PizzaConverterService pizzaConverterService;
    @Autowired
    PizzaCompleterService pizzaCompleterService;
    @Autowired
    PriceAdjustmentService priceAdjustmentService;
    @Autowired
    IngredientProducer ingredientProducer;
    @Autowired
    PizzaProducer pizzaProducer;
    @Autowired
    CurrencyProducer currencyProducer;
    @Autowired
    PriceProducer priceProducer;

    @RequestMapping("/ingredient/{id}/{currencies}")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<String> getIngredientById(@PathVariable(value = "id") Long id, @PathVariable String currencies) throws JSONException, JsonProcessingException {
        ResponseEntity<IngredientDTO> receivedDTO = ingredientProducer.getIngredientByID(id);
        IngredientDTO ingredientDTO = receivedDTO.getBody();
        String ingredientAsString = ingredientConverterService.mapIngredientDTOtoJson(ingredientDTO);
        double rate = adjustCurrency(currencies);
        if(rate != 1.0) {
            String adjustedIngredient = priceAdjustmentService.adjustPrice(ingredientAsString, rate);
            return new ResponseEntity<>(adjustedIngredient, HttpStatus.OK);
        }

        return new ResponseEntity<>(ingredientAsString, HttpStatus.OK);
    }
/*
    @RequestMapping("/ingredient/{currencies}")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<List<String>> getIngredients(@PathVariable String currencies) throws JsonProcessingException, JSONException {
        ResponseEntity<List<IngredientDTO>> receivedDTOList = ingredientProducer.getIngredients();
        List<IngredientDTO> ingredientDTOList = receivedDTOList.getBody();
        List<String> ingredientListAsString = ingredientConverterService.mapIngredientDTOListToJson(ingredientDTOList);
        double rate = adjustCurrency(currencies);
        if(rate != 1.0) {
            List<String> adjustedIngredientList = priceAdjustmentService.adjustListPrice(ingredientListAsString, rate);
            return new ResponseEntity<>(adjustedIngredientList, HttpStatus.OK);
        }

        return new ResponseEntity<>(ingredientListAsString, HttpStatus.OK);
    }

    @RequestMapping("/pizza/{id}/{currencies}")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<String> getPizzaById(@PathVariable(value = "id") Long id, @PathVariable String currencies) throws JSONException, JsonProcessingException {
        ResponseEntity<PizzaDTO> receivedDTO = pizzaProducer.getPizzaByID(id);
        PizzaDTO pizzaDTO = receivedDTO.getBody();

        ResponseEntity<List<IngredientDTO>> receivedDTOList = ingredientProducer.getIngredients();
        List<IngredientDTO> ingredientDTOList = receivedDTOList.getBody();
        CompletePizzaDTO pizzaWithIngredients = pizzaCompleterService.completeIngredientList(pizzaDTO, ingredientDTOList);

        ResponseEntity<Map<Long,Double>> receivedPrice = priceProducer.getPrice(pizzaDTO);
        Map<Long,Double> priceMap = receivedPrice.getBody();
        double price = priceMap.get(id);
        CompletePizzaDTO completePizza = pizzaCompleterService.addPriceToPizza(pizzaWithIngredients, price);

        String pizzaAsString = pizzaConverterService.mapCompletePizzaDTOtoJson(completePizza);

        double rate = adjustCurrency(currencies);
        if(rate != 1.0) {
            String adjustedPizza = priceAdjustmentService.adjustPrice(pizzaAsString, rate);
            return new ResponseEntity<>(adjustedPizza, HttpStatus.OK);
        }

        return new ResponseEntity<>(pizzaAsString, HttpStatus.OK);
    }
*/
    @RequestMapping(path = "/pizza/{currencies}", produces = "application/json")
    @GetMapping
    @RolesAllowed("user")
    public ResponseEntity<String> getPizzas(@PathVariable String currencies) throws JSONException, JsonProcessingException {
        ResponseEntity<List<PizzaDTO>> receivedDTOList = pizzaProducer.getPizzas();
        System.out.println(receivedDTOList);
        List<PizzaDTO> pizzaDTOList = receivedDTOList.getBody();

        ResponseEntity<List<IngredientDTO>> receivedIngredientList = ingredientProducer.getIngredients();
        List<IngredientDTO> ingredientDTOList = receivedIngredientList.getBody();
        List<CompletePizzaDTO> pizzasWithIngredients = pizzaCompleterService.completeIngredientListForPizzaList(pizzaDTOList, ingredientDTOList);

        Map<Long, Double> idPriceMapForAllPizzas = new HashMap<>();
        for (PizzaDTO x : pizzaDTOList) {
            ResponseEntity<Map<Long,Double>> receivedPrice = priceProducer.getPrice(x);
            Map<Long,Double> priceMap = receivedPrice.getBody();
            Long id = x.getId();
            Double price = priceMap.get(id);
            idPriceMapForAllPizzas.put(id, price);
        }
        List<CompletePizzaDTO> pizzasWithPriceAndIngredients = pizzaCompleterService.addPricesToPizzaList(pizzasWithIngredients, idPriceMapForAllPizzas);

        double rate = adjustCurrency(currencies);
        String returnString;
        if(rate != 1.0) {
            List<CompletePizzaDTO> adjustedPizzaList = priceAdjustmentService.adjustListPrice(pizzasWithPriceAndIngredients, rate);
            returnString = pizzaConverterService.mapCompletePizzaDTOListToJson(adjustedPizzaList);
        }
        returnString = pizzaConverterService.mapCompletePizzaDTOListToJson(pizzasWithPriceAndIngredients);

        return new ResponseEntity<>(returnString, HttpStatus.OK);
    }

    @PostMapping("/createPizza/{currencies}")
    @GetMapping
    @RolesAllowed("user")
    public HttpStatus createPizza(@RequestBody JSONObject frontendPizza, @PathVariable String currencies) throws JSONException, JsonProcessingException {
        Long id = new Random().nextLong(); //zwischen 1 und 10000
        frontendPizza.put("id", id);
        String pizzaString = frontendPizza.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        PizzaDTO pizzaDTO = objectMapper.readValue(pizzaString, PizzaDTO.class);

        ResponseEntity<PizzaDTO> responsePizza = pizzaProducer.createPizza(pizzaDTO);

        return responsePizza.getStatusCode();
    }

    public double adjustCurrency(String currency) {
        if(priceAdjustmentService.checkIfPriceNeedsAdjusting(currency)) {
            String currencies = "EUR_" + currency;
            ResponseEntity<Double> receivedRate = currencyProducer.getCurrency(currencies);
            double rate = receivedRate.getBody();
            return rate;
        }
        return 1.0;
    }
}