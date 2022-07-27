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

@CrossOrigin
@RestController
@RequestMapping
public class ApiGatewayController {

    @Autowired
    private IngredientConverterService ingredientConverterService;
    @Autowired
    private PizzaConverterService pizzaConverterService;
    @Autowired
    private PizzaCompleterService pizzaCompleterService;
    @Autowired
    private PriceAdjustmentService priceAdjustmentService;

    @Autowired
    private IngredientProducer ingredientProducer;
    @Autowired
    private PizzaProducer pizzaProducer;
    @Autowired
    private PriceProducer priceProducer;
    @Autowired
    private CurrencyProducer currencyProducer;


    @GetMapping(path = "/ingredient/{id}/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getIngredientById(@PathVariable(value = "id") Long id, @PathVariable String currency) throws JSONException, JsonProcessingException {
        ResponseEntity<IngredientDTO> receivedDTO = ingredientProducer.getIngredientByID(id);
        IngredientDTO ingredientDTO = receivedDTO.getBody();
        double rate = adjustCurrency(currency);
        String ingredientAsString;

        if(rate != 1.0) {
            IngredientDTO adjustedIngredient = priceAdjustmentService.adjustIngredientPrice(ingredientDTO, rate);
            ingredientAsString = ingredientConverterService.mapIngredientDTOToJson(adjustedIngredient);
        }
        else ingredientAsString = ingredientConverterService.mapIngredientDTOToJson(ingredientDTO);

        return new ResponseEntity<>(ingredientAsString, HttpStatus.OK);
    }

    @GetMapping(path = "/ingredient/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getIngredients(@PathVariable String currencies) throws JsonProcessingException, JSONException {
        ResponseEntity<List<IngredientDTO>> receivedDTOList = ingredientProducer.getIngredients();
        List<IngredientDTO> ingredientDTOList = receivedDTOList.getBody();
        double rate = adjustCurrency(currencies);
        String ingredientListAsString;
        if(rate != 1.0) {
            List<IngredientDTO> adjustedIngredientList = priceAdjustmentService.adjustIngredientListPrice(ingredientDTOList, rate);
            ingredientListAsString = ingredientConverterService.mapIngredientDTOListToJson(adjustedIngredientList);
        }
        else ingredientListAsString = ingredientConverterService.mapIngredientDTOListToJson(ingredientDTOList);

        return new ResponseEntity<>(ingredientListAsString, HttpStatus.OK);
    }

    @GetMapping(path = "/pizza/{id}/{currency}", produces = "application/json")
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
        pizzaWithIngredients.setPrice(price);
        String pizzaAsString;

        double rate = adjustCurrency(currencies);
        if(rate != 1.0) {
            CompletePizzaDTO adjustedPizza = priceAdjustmentService.adjustPizzaPrice(pizzaWithIngredients, rate);
            pizzaAsString = pizzaConverterService.mapCompletePizzaDTOToJson(adjustedPizza);
        }
        else pizzaAsString = pizzaConverterService.mapCompletePizzaDTOToJson(pizzaWithIngredients);

        return new ResponseEntity<>(pizzaAsString, HttpStatus.OK);
    }

    @GetMapping(path = "/pizza/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getPizzas(@PathVariable String currencies) throws JsonProcessingException {
        ResponseEntity<List<PizzaDTO>> receivedDTOList = pizzaProducer.getPizzas();
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
            List<CompletePizzaDTO> adjustedPizzaList = priceAdjustmentService.adjustPizzaListPrice(pizzasWithPriceAndIngredients, rate);
            returnString = pizzaConverterService.mapCompletePizzaDTOListToJson(adjustedPizzaList);
        }
        returnString = pizzaConverterService.mapCompletePizzaDTOListToJson(pizzasWithPriceAndIngredients);

        return new ResponseEntity<>(returnString, HttpStatus.OK);
    }

    @PostMapping("/createPizza/{currency}")
    @RolesAllowed("user")
    public ResponseEntity<PizzaDTO> createPizza(@RequestBody JSONObject frontendPizza, @PathVariable String currencies) throws JSONException, JsonProcessingException {
        Long id = new Random().nextLong(); //zwischen 1 und 10000
        frontendPizza.put("id", id);
        String pizzaString = frontendPizza.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        PizzaDTO pizzaDTO = objectMapper.readValue(pizzaString, PizzaDTO.class);

        ResponseEntity<PizzaDTO> responsePizza = pizzaProducer.createPizza(pizzaDTO);

        return responsePizza;
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