package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PizzaConverterService {

    public String mapCompletePizzaDTOListToJson(List<CompletePizzaDTO> completePizzaDTOList) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(completePizzaDTOList);
    }

    public String mapCompletePizzaDTOToJson(CompletePizzaDTO completePizzaDTO) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(completePizzaDTO);
    }
}