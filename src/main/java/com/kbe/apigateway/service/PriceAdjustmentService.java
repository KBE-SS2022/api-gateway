package com.kbe.apigateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kbe.apigateway.dto.IngredientDTO;
import com.kbe.apigateway.dto.PizzaDTO;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class PriceAdjustmentService {

    private double originalPrice;

    public String adjustPrice(String dto, double rate) throws JSONException {
        JSONObject jsonDTO = new JSONObject(dto);
        originalPrice = jsonDTO.getDouble("price");
        jsonDTO.put("price", originalPrice * rate);

        return jsonDTO.toString();
    }

    public List<String> adjustListPrice(List<String> dtoList, double rate) throws JSONException {
        for(String x : dtoList) {
            adjustPrice(x, rate);
        }
        return dtoList;
    }

    public boolean checkIfPriceNeedsAdjusting(String currencies) {
        String[] currencyArray = currencies.split("_");
        if(currencyArray[0] == currencyArray[1])
            return false;
        else
            return true;
    }

}
