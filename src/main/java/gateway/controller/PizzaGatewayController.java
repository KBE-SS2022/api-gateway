package gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.dto.IngredientDTO;
import gateway.dto.PizzaDTO;
import gateway.rabbitmq.producer.IngredientProducer;
import gateway.rabbitmq.producer.PizzaProducer;
import gateway.service.PizzaGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@RequestMapping
public class PizzaGatewayController {

    @Autowired
    private PizzaProducer pizzaProducer;
    @Autowired
    private IngredientProducer ingredientProducer;
    @Autowired
    private PizzaGatewayService pizzaGatewayService;

    @GetMapping(path = "/pizza/{id}/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getPizzaById(@PathVariable(value = "id") Long id, @PathVariable String currency) throws JsonProcessingException {
        ResponseEntity<PizzaDTO> pizzaEntity = pizzaProducer.getPizzaByID(id);
        HttpStatus pizzaStatus = pizzaEntity.getStatusCode();
        if(pizzaStatus.equals(HttpStatus.NOT_FOUND)) return new ResponseEntity<>(pizzaStatus);
        PizzaDTO pizzaDTO = pizzaEntity.getBody();

        ResponseEntity<List<IngredientDTO>> ingredientListEntity = ingredientProducer.getIngredients();
        HttpStatus ingredientStatus = ingredientListEntity.getStatusCode();
        if(ingredientStatus.equals(HttpStatus.INTERNAL_SERVER_ERROR)) return new ResponseEntity<>(ingredientStatus);
        List<IngredientDTO> ingredientDTOList = ingredientListEntity.getBody();

        String pizzaJSONString = pizzaGatewayService.getPizza(pizzaDTO, ingredientDTOList, currency);
        return new ResponseEntity<>(pizzaJSONString, HttpStatus.OK);
    }

    @GetMapping(path = "/pizzas/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getPizzas(@PathVariable String currency) throws JsonProcessingException {
        ResponseEntity<List<PizzaDTO>> pizzaListEntity = pizzaProducer.getPizzas();
        HttpStatus pizzaStatus = pizzaListEntity.getStatusCode();
        if(pizzaStatus.equals(HttpStatus.INTERNAL_SERVER_ERROR)) return new ResponseEntity<>(pizzaStatus);
        List<PizzaDTO> pizzaDTOList = pizzaListEntity.getBody();

        ResponseEntity<List<IngredientDTO>> ingredientListEntity = ingredientProducer.getIngredients();
        HttpStatus ingredientStatus = ingredientListEntity.getStatusCode();
        if(ingredientStatus.equals(HttpStatus.INTERNAL_SERVER_ERROR)) return new ResponseEntity<>(ingredientStatus);
        List<IngredientDTO> ingredientDTOList = ingredientListEntity.getBody();

        String pizzaListAsString = pizzaGatewayService.getPizzas(pizzaDTOList, ingredientDTOList, currency);
        return new ResponseEntity<>(pizzaListAsString, HttpStatus.OK);
    }

    @PostMapping(path = "/createPizza", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<PizzaDTO> createPizza(@RequestBody PizzaDTO pizza) {
        Long id = new Random().nextLong(999) + 1; //zwischen 1 und 1000
        pizza.setId(id);
        return pizzaProducer.createPizza(pizza);
    }
}