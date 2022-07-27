package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PizzaConverterService {

    public String completeIngredientList(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs)
            throws JSONException, JsonProcessingException {
        List<IngredientDTO> pizzaIngredients = findIngredients(pizzaDTO, ingredientDTOs);
        JSONObject properPizza = removeIdPriceMap(pizzaDTO);
        properPizza.put("ingredients", pizzaIngredients);

        return properPizza.toString();
    }

    public List<String> completeIngredientListForPizzaList(List<PizzaDTO> pizzaDTOList, List<IngredientDTO> ingredientDTOs)
            throws JSONException, JsonProcessingException {
        List<String> properPizzaList = new ArrayList<>();

        for(PizzaDTO x : pizzaDTOList) {
            String properPizza = completeIngredientList(x, ingredientDTOs);
            properPizzaList.add(properPizza);
        }

        return properPizzaList;
    }

    private List<IngredientDTO> findIngredients(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs) {
        Map<Long, Double> ingredientsMap = pizzaDTO.getIngredientIdToPrice();
        Set<Long> ingredientIds = ingredientsMap.keySet();
        List<IngredientDTO> matchingIngredients = ingredientDTOs.stream()
                .filter(x -> ingredientIds.contains(x.getId()))
                .toList();
        return matchingIngredients;
    }

    private JSONObject removeIdPriceMap(PizzaDTO pizzaDTO) throws JsonProcessingException, JSONException {
        ObjectMapper mapper = new ObjectMapper();
        String pizzaString = mapper.writeValueAsString(pizzaDTO);
        JSONObject jsonObject = new JSONObject(pizzaString);
        jsonObject.remove("ingredientIdToPrice");
        return jsonObject;
    }

    public String addPriceToPizza(JSONObject pizza, double price) throws JSONException {
        pizza.put("price", price);

        return pizza.toString();
    }

}
