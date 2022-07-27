package gateway.rabbitmq.producer;

import gateway.rabbitmq.config.Constant;
import gateway.dto.IngredientDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/rabbit")
public class IngredientProducer {

    @Autowired
    private RabbitTemplate template;

    @GetMapping(value = "/ingredients", produces = "application/json")
    public ResponseEntity<List<IngredientDTO>> getIngredients() {
        List<IngredientDTO> receivedIngredientDTOs = template.convertSendAndReceiveAsType(
                Constant.INGREDIENT_EXCHANGE,
                Constant.GETALL_KEY,
                "getall.ingredient",
                new ParameterizedTypeReference<>() {}
        );
        if(receivedIngredientDTOs == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(receivedIngredientDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/ingredient/{id}", produces = "application/json")
    public ResponseEntity<IngredientDTO> getIngredientByID(@PathVariable(value = "id") Long id) {
        IngredientDTO receivedDTO = template.convertSendAndReceiveAsType(
                Constant.INGREDIENT_EXCHANGE,
                Constant.GET_KEY,
                id,
                new ParameterizedTypeReference<>() {}
        );
        if(receivedDTO == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(receivedDTO, HttpStatus.OK);
    }
}