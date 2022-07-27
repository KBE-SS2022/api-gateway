package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PizzaCompleterService {

    public CompletePizzaDTO completeIngredientList(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs) {

        CompletePizzaDTO completePizzaDTO = new CompletePizzaDTO();
        Long id = pizzaDTO.getId();
        String name = pizzaDTO.getName();
        List<IngredientDTO> pizzaIngredients = findIngredients(pizzaDTO, ingredientDTOs);
        completePizzaDTO.setId(id);
        completePizzaDTO.setName(name);
        completePizzaDTO.setIngredientDTOList(pizzaIngredients);

        return completePizzaDTO;
    }

    public List<CompletePizzaDTO> completeIngredientListForPizzaList(List<PizzaDTO> pizzaDTOList, List<IngredientDTO> ingredientDTOs) {
        List<CompletePizzaDTO> completePizzaDTOList = new ArrayList<>();

        for(PizzaDTO x : pizzaDTOList) {
            CompletePizzaDTO completePizza = completeIngredientList(x, ingredientDTOs);
            completePizzaDTOList.add(completePizza);
        }

        return completePizzaDTOList;
    }

    private List<IngredientDTO> findIngredients(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs) {
        Map<Long, Double> ingredientsMap = pizzaDTO.getIngredientIdToPrice();
        Set<Long> ingredientIds = ingredientsMap.keySet();
        List<IngredientDTO> matchingIngredients = ingredientDTOs.stream()
                .filter(x -> ingredientIds.contains(x.getId()))
                .toList();

        return matchingIngredients;
    }

    public CompletePizzaDTO addPriceToPizza(CompletePizzaDTO pizza, double price) {
        pizza.setPrice(price);

        return pizza;
    }

    public List<CompletePizzaDTO> addPricesToPizzaList(List<CompletePizzaDTO> pizzaList, Map<Long, Double> idPriceMap) {
       for (CompletePizzaDTO x : pizzaList) {
           Long id = x.getId();
           Double price = idPriceMap.get(id);
           x.setPrice(price);
       }

       return pizzaList;
    }

}
