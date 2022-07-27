package com.kbe.apigateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import com.kbe.apigateway.messageproducer.producer.IngredientProducer;
import com.kbe.apigateway.messageproducer.producer.PizzaProducer;
import com.kbe.apigateway.service.PizzaGatewayService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping
public class PizzaGatewayController {

    @Autowired
    private PizzaProducer pizzaProducer;
    @Autowired
    private IngredientProducer ingredientProducer;
    @Autowired
    private PizzaGatewayService pizzaGatewayService;

    @GetMapping(path = "/pizza/{id}/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getPizzaById(@PathVariable(value = "id") Long id, @PathVariable String currency) throws JSONException, JsonProcessingException {
        ResponseEntity<PizzaDTO> receivedDTO = pizzaProducer.getPizzaByID(id);
        PizzaDTO pizzaDTO = receivedDTO.getBody();
        ResponseEntity<List<IngredientDTO>> receivedDTOList = ingredientProducer.getIngredients();
        List<IngredientDTO> ingredientDTOList = receivedDTOList.getBody();

        String pizzaJSONString = pizzaGatewayService.getPizza(pizzaDTO, ingredientDTOList, currency);
        return new ResponseEntity<>(pizzaJSONString, HttpStatus.OK);
    }

    @GetMapping(path = "/pizzas/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getPizzas(@PathVariable String currency) throws JsonProcessingException {
        ResponseEntity<List<PizzaDTO>> receivedDTOList = pizzaProducer.getPizzas();
        List<PizzaDTO> pizzaDTOList = receivedDTOList.getBody();
        ResponseEntity<List<IngredientDTO>> receivedIngredientList = ingredientProducer.getIngredients();
        List<IngredientDTO> ingredientDTOList = receivedIngredientList.getBody();

        String pizzaListAsString = pizzaGatewayService.getPizzas(pizzaDTOList, ingredientDTOList, currency);
        return new ResponseEntity<>(pizzaListAsString, HttpStatus.OK);
    }

    @PostMapping(path = "/createPizza/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<PizzaDTO> createPizza(@RequestBody JSONObject frontendPizza, @PathVariable String currency) throws JSONException, JsonProcessingException {
        Long id = new Random().nextLong(999) + 1; //zwischen 1 und 1000
        frontendPizza.put("id", id);
        String pizzaString = frontendPizza.toString();
        ObjectMapper objectMapper = new ObjectMapper();
        PizzaDTO pizzaDTO = objectMapper.readValue(pizzaString, PizzaDTO.class);

        ResponseEntity<PizzaDTO> responsePizza = pizzaProducer.createPizza(pizzaDTO);

        return responsePizza;
    }
}