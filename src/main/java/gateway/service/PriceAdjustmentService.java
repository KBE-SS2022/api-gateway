package gateway.service;

import gateway.dto.CompletePizzaDTO;
import gateway.dto.IngredientDTO;
import gateway.rabbitmq.producer.CurrencyProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PriceAdjustmentService {

    @Autowired
    private CurrencyProducer currencyProducer;

    public CompletePizzaDTO adjustPizzaPrice(CompletePizzaDTO pizza, double rate) {
        Double adjustedPrice = pizza.getPrice() * rate;
        pizza.setPrice(adjustedPrice);
        return pizza;
    }

    public IngredientDTO adjustIngredientPrice(IngredientDTO ingredient, double rate) {
        double adjustedPrice = ingredient.getPrice() * rate;
        ingredient.setPrice(adjustedPrice);
        return ingredient;
    }

    public List<CompletePizzaDTO> adjustPizzasPrice(List<CompletePizzaDTO> pizzaList, double rate) {
        return pizzaList.stream()
                .map(pizza -> adjustPizzaPrice(pizza, rate))
                .collect(Collectors.toList());
    }

    public List<IngredientDTO> adjustIngredientsPrice(List<IngredientDTO> indredientList, double rate) {
        return indredientList.stream()
                .map(ingredient -> adjustIngredientPrice(ingredient, rate))
                .collect(Collectors.toList());
    }

    public double adjustCurrency(String currency) {
        if(isCurrencyChanged(currency)) {
            String currencies = "EUR_" + currency;
            ResponseEntity<Double> currencyEntity = currencyProducer.getCurrency(currencies);
            HttpStatus status = currencyEntity.getStatusCode();
            if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR)) return 1.0;
            double rate = currencyEntity.getBody();
            return rate;
        }
        return 1.0;
    }

    private boolean isCurrencyChanged(String currency) {
        return !currency.equals("EUR");
    }
}