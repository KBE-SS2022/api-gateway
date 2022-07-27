package gateway.service;

import gateway.dto.CompletePizzaDTO;
import gateway.dto.IngredientDTO;
import gateway.dto.PizzaDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PizzaCompleterService {

    public CompletePizzaDTO completeIngredients(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs) {
        CompletePizzaDTO completePizzaDTO = new CompletePizzaDTO();
        Long id = pizzaDTO.getId();
        String name = pizzaDTO.getName();
        List<IngredientDTO> pizzaIngredients = findIngredients(pizzaDTO, ingredientDTOs);

        completePizzaDTO.setId(id);
        completePizzaDTO.setName(name);
        completePizzaDTO.setIngredients(pizzaIngredients);

        return completePizzaDTO;
    }

    public List<CompletePizzaDTO> completeIngredientsForPizzas(List<PizzaDTO> pizzaDTOList, List<IngredientDTO> ingredientDTOs) {
        List<CompletePizzaDTO> completePizzaDTOList = pizzaDTOList.stream()
                .map(pizzaDTO -> completeIngredients(pizzaDTO, ingredientDTOs))
                .collect(Collectors.toList());
        return completePizzaDTOList;
    }

    public List<CompletePizzaDTO> addPricesToPizzas(List<CompletePizzaDTO> pizzaList, Map<Long, Double> idPriceMap) {
        List<CompletePizzaDTO> pizzaDTOListWithPrices = pizzaList.stream()
                .map(pizza -> {
                    Long id = pizza.getId();
                    Double price = idPriceMap.get(id);
                    pizza.setPrice(price);
                    return pizza;
                })
                .collect(Collectors.toList());
       return pizzaDTOListWithPrices;
    }

    public CompletePizzaDTO addPriceToPizza(CompletePizzaDTO pizza, Map<Long,Double> idPriceMap) {
        Long id = pizza.getId();
        double price = idPriceMap.get(id);
        pizza.setPrice(price);
        return pizza;
    }

    private List<IngredientDTO> findIngredients(PizzaDTO pizzaDTO, List<IngredientDTO> ingredientDTOs) {
        Map<Long, Double> ingredientsMap = pizzaDTO.getIngredientIdToPrice();
        Set<Long> ingredientIds = ingredientsMap.keySet();
        List<IngredientDTO> matchingIngredients = ingredientDTOs.stream()
                .filter(ingredient -> ingredientIds.contains(ingredient.getId()))
                .toList();

        return matchingIngredients;
    }
}