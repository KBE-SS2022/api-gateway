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

    public String mapIngredientListToJson(List<IngredientDTO> IngredientDTOList) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(IngredientDTOList);
    }

    public String mapIngredientToJson(IngredientDTO ingredientDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(ingredientDTO);
    }
}