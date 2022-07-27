package gateway.rabbitmq.producer;

import gateway.rabbitmq.config.Constant;
import gateway.dto.PizzaDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/rabbit")
public class PizzaProducer {

    @Autowired
    private RabbitTemplate template;

    @GetMapping(value = "/pizzas", produces = "application/json")
    public ResponseEntity<List<PizzaDTO>> getPizzas() {
        List<PizzaDTO> receivedPizzaDTOs = template.convertSendAndReceiveAsType(
                Constant.PIZZA_EXCHANGE,
                Constant.GETALL_KEY,
                "getall.pizza",
                new ParameterizedTypeReference<>() {}
        );
        if(receivedPizzaDTOs == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(receivedPizzaDTOs, HttpStatus.OK);
    }

    @GetMapping(value = "/pizza/{id}", produces = "application/json")
    public ResponseEntity<PizzaDTO> getPizzaByID(@PathVariable(value = "id") Long id) {
        PizzaDTO receivedDTO = template.convertSendAndReceiveAsType(
                Constant.PIZZA_EXCHANGE,
                Constant.GET_KEY,
                id,
                new ParameterizedTypeReference<>() {}
        );
        if(receivedDTO == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(receivedDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/pizza/create", produces = "application/json")
    public ResponseEntity<PizzaDTO> createPizza(@RequestBody PizzaDTO pizzaDTO) {
        PizzaDTO receivedDTO = template.convertSendAndReceiveAsType(
                Constant.PIZZA_EXCHANGE,
                Constant.CREATE_KEY,
                pizzaDTO,
                new ParameterizedTypeReference<>() {}
        );
        if(receivedDTO == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(receivedDTO, HttpStatus.CREATED);
    }
}