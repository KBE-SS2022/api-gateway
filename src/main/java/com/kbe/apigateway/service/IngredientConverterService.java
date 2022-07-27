package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientConverterService {

    public String mapIngredientDTOListToJson(List<IngredientDTO> IngredientDTOList) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        String ingredientDTOListAsString = mapper.writeValueAsString(IngredientDTOList);
        return ingredientDTOListAsString;
    }

    public String mapIngredientDTOToJson(IngredientDTO ingredientDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String ingredientDTOAsString = mapper.writeValueAsString(ingredientDTO);
        return ingredientDTOAsString;
    }
}