package gateway.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.dto.CompletePizzaDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
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