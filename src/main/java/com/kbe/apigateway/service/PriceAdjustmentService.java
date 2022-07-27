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

    private double originalPrice;

    public String adjustPrice(String dto, double rate) throws JSONException {
        JSONObject jsonDTO = new JSONObject(dto);
        originalPrice = jsonDTO.getDouble("price");
        jsonDTO.put("price", originalPrice * rate);

        return jsonDTO.toString();
    }

   /* public List<String> adjustListPrice(List<String> dtoList, double rate) throws JSONException {
        for(String x : dtoList) {
            adjustPrice(x, rate);
        }
        return dtoList;
    }*/

    public CompletePizzaDTO adjustPrice(CompletePizzaDTO dto, double rate) {
        Double adjustedPrice = dto.getPrice() * rate;
        CompletePizzaDTO adjustedPizza = new CompletePizzaDTO(
                dto.getId(),
                dto.getName(),
                adjustedPrice,
                dto.getIngredients());
        return  adjustedPizza;
    }

    public List<CompletePizzaDTO> adjustListPrice(List<CompletePizzaDTO> dtoList, double rate) {
        return dtoList.stream()
                .map(pizzaDTO -> adjustPrice(pizzaDTO, rate))
                .collect(Collectors.toList());
    }

    public boolean checkIfPriceNeedsAdjusting(String currency) {
        if(currency.equals("EUR")) return false;
        return true;
    }
}