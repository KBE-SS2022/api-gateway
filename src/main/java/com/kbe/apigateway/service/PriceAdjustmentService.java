package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.json.JSONException;
import org.json.JSONObject;

public class PriceAdjustmentService {

    public String adjustPizzaPrice(JSONObject pizza, double rate) throws JSONException {
        double originalPrice = pizza.getDouble("price");
        pizza.put("price", originalPrice * rate);

        return pizza.toString();
    }

    public String adjustIngredientPrice(JSONObject ingredient, double rate) throws JSONException {
        double originalPrice = ingredient.getDouble("price");
        ingredient.put("price", originalPrice * rate);

        return ingredient.toString();
    }
}
