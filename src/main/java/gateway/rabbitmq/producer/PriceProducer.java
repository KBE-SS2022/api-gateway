package gateway.rabbitmq.producer;

import gateway.rabbitmq.config.Constant;
import gateway.dto.PizzaDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/rabbit")
public class PriceProducer {

    @Autowired
    private RabbitTemplate template;

    @GetMapping(value = "/price", produces = "application/json")
    public ResponseEntity<Map<Long,Double>> getPrice(@RequestBody PizzaDTO pizzaDTO) {
        Map<Long,Double> receivedPrice = template.convertSendAndReceiveAsType(
                Constant.PRICE_EXCHANGE,
                Constant.GET_KEY,
                pizzaDTO,
                new ParameterizedTypeReference<>() {}
        );
        if(receivedPrice == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(receivedPrice, HttpStatus.OK);
    }
}