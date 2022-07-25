package com.kbe.apigateway.service;

import org.json.JSONException;
import org.json.JSONObject;

public class PriceAdderService {

    public JSONObject addPriceToPizza(JSONObject pizza, double price) throws JSONException {
        pizza.put("price", price);

        return pizza;
    }
}
