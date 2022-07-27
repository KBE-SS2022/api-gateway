package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;

import java.util.ArrayList;
import java.util.List;

public class PizzaConverterService {

    public String mapCompletePizzaDTOtoJson(CompletePizzaDTO completePizzaDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String pizzaAsJson = objectMapper.writeValueAsString(completePizzaDTO);

        return pizzaAsJson;
    }

    public List<String> mapCompletePizzaDTOListToJson(List<CompletePizzaDTO> completePizzaDTOList) throws JsonProcessingException{
        List<String> pizzaListAsJson = new ArrayList<>();
        for(CompletePizzaDTO x : completePizzaDTOList){
            pizzaListAsJson.add(mapCompletePizzaDTOtoJson(x));
        }
        return pizzaListAsJson;
    }
}
