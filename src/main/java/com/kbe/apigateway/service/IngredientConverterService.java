package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.IngredientDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IngredientConverterService {

    public String mapIngredientDTOtoJson(IngredientDTO ingredientDTO) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String ingredientAsJson = objectMapper.writeValueAsString(ingredientDTO);

        return ingredientAsJson;
    }

    public List<String> mapIngredientDTOListToJson(List<IngredientDTO> ingredientDTOList) throws JsonProcessingException{
        List<String> ingredientListAsJson = new ArrayList<>();
        for(IngredientDTO x : ingredientDTOList){
            ingredientListAsJson.add(mapIngredientDTOtoJson(x));
        }
        return ingredientListAsJson;
    }

}
