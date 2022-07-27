package com.kbe.apigateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.messageproducer.producer.CurrencyProducer;
import com.kbe.apigateway.messageproducer.producer.IngredientProducer;
import com.kbe.apigateway.messageproducer.producer.PizzaProducer;
import com.kbe.apigateway.messageproducer.producer.PriceProducer;
import com.kbe.apigateway.service.IngredientConverterService;
import com.kbe.apigateway.service.IngredientGatewayService;
import com.kbe.apigateway.service.PriceAdjustmentService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping
public class IngredientGatewayController {

    @Autowired
    private IngredientProducer ingredientProducer;
    @Autowired
    private IngredientGatewayService ingredientGatewayService;

    @GetMapping(path = "/ingredient/{id}/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getIngredientById(@PathVariable(value = "id") Long id, @PathVariable String currency) throws JSONException, JsonProcessingException {
        ResponseEntity<IngredientDTO> receivedDTO = ingredientProducer.getIngredientByID(id);
        IngredientDTO ingredientDTO = receivedDTO.getBody();

        String ingredientAsString = ingredientGatewayService.getIngredient(ingredientDTO, currency);
        return new ResponseEntity<>(ingredientAsString, HttpStatus.OK);
    }

    @GetMapping(path = "/ingredients/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getIngredients(@PathVariable String currency) throws JsonProcessingException {
        ResponseEntity<List<IngredientDTO>> receivedDTOList = ingredientProducer.getIngredients();
        List<IngredientDTO> ingredientDTOList = receivedDTOList.getBody();

        String ingredientListAsString = ingredientGatewayService.getIngredients(ingredientDTOList, currency);
        return new ResponseEntity<>(ingredientListAsString, HttpStatus.OK);
    }
}