package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransformationService {

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    public String mapIngredientDTOtoJson(IngredientDTO ingredientDTO) throws JsonProcessingException {
        String ingredientAsJson = objectMapper.writeValueAsString(ingredientDTO);

        return ingredientAsJson;
    }

    public String mapPizzaDTOtoJson(PizzaDTO pizzaDTO) throws JsonProcessingException {
        String pizzaAsJson = objectMapper.writeValueAsString(pizzaDTO);

        return pizzaAsJson;
    }

}
