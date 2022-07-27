package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.CompletePizzaDTO;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceAdjustmentService {

    public CompletePizzaDTO adjustPizzaPrice(CompletePizzaDTO dto, double rate) {
        Double adjustedPrice = dto.getPrice() * rate;
        dto.setPrice(adjustedPrice);
        return dto;
    }

    public IngredientDTO adjustIngredientPrice(IngredientDTO dto, double rate) {
        double adjustedPrice = dto.getPrice() * rate;
        dto.setPrice(adjustedPrice);
        return dto;
    }

    public List<CompletePizzaDTO> adjustPizzaListPrice(List<CompletePizzaDTO> dtoList, double rate) {
        return dtoList.stream()
                .map(pizza -> adjustPizzaPrice(pizza, rate))
                .collect(Collectors.toList());
    }

    public List<IngredientDTO> adjustIngredientListPrice(List<IngredientDTO> dtoList, double rate) {
        return dtoList.stream()
                .map(ingredient -> adjustIngredientPrice(ingredient, rate))
                .collect(Collectors.toList());
    }

    public boolean checkIfPriceNeedsAdjusting(String currency) {
        return !currency.equals("EUR");
    }
}