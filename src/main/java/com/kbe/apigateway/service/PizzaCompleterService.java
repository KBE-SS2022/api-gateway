package com.kbe.apigateway.service;

import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PizzaCompleterService {

    private List<IngredientDTO> findIngredients(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs) {
        Map<Long, Double> ingredientsMap = pizzaDTO.getIngredientIdToPrice();
        Set<Long> ingredientIds = ingredientsMap.keySet();
        List<IngredientDTO> matchingIngredients = ingredientDTOs.stream()
                .filter(ingredient -> ingredientIds.contains(ingredient.getId()))
                .toList();

        return matchingIngredients;
    }

    public CompletePizzaDTO completeIngredientList(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs) {
        CompletePizzaDTO completePizzaDTO = new CompletePizzaDTO();
        Long id = pizzaDTO.getId();
        String name = pizzaDTO.getName();
        List<IngredientDTO> pizzaIngredients = findIngredients(pizzaDTO, ingredientDTOs);

        completePizzaDTO.setId(id);
        completePizzaDTO.setName(name);
        completePizzaDTO.setIngredients(pizzaIngredients);

        return completePizzaDTO;
    }

    public List<CompletePizzaDTO> completeIngredientListForPizzaList(List<PizzaDTO> pizzaDTOList, List<IngredientDTO> ingredientDTOs) {
        List<CompletePizzaDTO> completePizzaDTOList = pizzaDTOList.stream()
                .map(pizzaDTO -> completeIngredientList(pizzaDTO, ingredientDTOs))
                .collect(Collectors.toList());
        return completePizzaDTOList;
    }

    public List<CompletePizzaDTO> addPricesToPizzaList(List<CompletePizzaDTO> pizzaList, Map<Long, Double> idPriceMap) {
        List<CompletePizzaDTO> pizzaDTOListWithPrices = pizzaList.stream()
                .map(pizza -> {
                    Long id = pizza.getId();
                    Double price = idPriceMap.get(id);
                    pizza.setPrice(price);
                    return pizza;
                })
                .collect(Collectors.toList());
       return pizzaDTOListWithPrices;
    }


}