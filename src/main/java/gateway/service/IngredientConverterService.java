package gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.dto.IngredientDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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