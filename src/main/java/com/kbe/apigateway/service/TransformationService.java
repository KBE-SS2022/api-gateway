package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.json.JSONException;
import org.json.JSONObject;

public class TransformationService {

    private ObjectMapper objectMapper = new ObjectMapper();

    public JSONObject mapIngredientDTOtoJson(IngredientDTO ingredientDTO) throws JsonProcessingException, JSONException {
        String ingredientAsJson = objectMapper.writeValueAsString(ingredientDTO);

        return new JSONObject(ingredientAsJson);
    }

    public JSONObject mapPizzaDTOtoJson(PizzaDTO pizzaDTO) throws JsonProcessingException, JSONException {
        String pizzaAsJson = objectMapper.writeValueAsString(pizzaDTO);

        return new JSONObject(pizzaAsJson);
    }
}
