package gateway.rabbitmq.producer;

import gateway.rabbitmq.config.Constant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rabbit")
public class CurrencyProducer {

    @Autowired
    private RabbitTemplate template;

    @GetMapping(value = "/currency", produces = "application/json")
    public ResponseEntity<Double> getCurrency(@RequestBody String currencies) {
        Double receivedCurrency = template.convertSendAndReceiveAsType(
                Constant.CURRENCY_EXCHANGE,
                Constant.GET_KEY,
                currencies,
                new ParameterizedTypeReference<>() {}
        );
        if(receivedCurrency == null) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(receivedCurrency, HttpStatus.OK);
    }
}