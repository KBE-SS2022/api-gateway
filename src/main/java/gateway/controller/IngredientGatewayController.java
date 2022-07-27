package gateway.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import gateway.dto.IngredientDTO;
import gateway.rabbitmq.producer.IngredientProducer;
import gateway.service.IngredientGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping
public class IngredientGatewayController {

    @Autowired
    private IngredientProducer ingredientProducer;
    @Autowired
    private IngredientGatewayService ingredientGatewayService;

    @GetMapping(path = "/ingredient/{id}/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getIngredientById(@PathVariable(value = "id") Long id, @PathVariable String currency) throws JsonProcessingException {
        ResponseEntity<IngredientDTO> ingredientEntity = ingredientProducer.getIngredientByID(id);
        HttpStatus ingredientStatus = ingredientEntity.getStatusCode();
        if(ingredientStatus.equals(HttpStatus.NOT_FOUND)) return new ResponseEntity<>(ingredientStatus);
        IngredientDTO ingredientDTO = ingredientEntity.getBody();

        String ingredientAsString = ingredientGatewayService.getIngredient(ingredientDTO, currency);
        return new ResponseEntity<>(ingredientAsString, HttpStatus.OK);
    }

    @GetMapping(path = "/ingredients/{currency}", produces = "application/json")
    @RolesAllowed("user")
    public ResponseEntity<String> getIngredients(@PathVariable String currency) throws JsonProcessingException {
        ResponseEntity<List<IngredientDTO>> ingredientListEntity = ingredientProducer.getIngredients();
        HttpStatus ingredientStatus = ingredientListEntity.getStatusCode();
        if(ingredientStatus.equals(HttpStatus.INTERNAL_SERVER_ERROR)) return new ResponseEntity<>(ingredientStatus);
        List<IngredientDTO> ingredientDTOList = ingredientListEntity.getBody();

        String ingredientListAsString = ingredientGatewayService.getIngredients(ingredientDTOList, currency);
        return new ResponseEntity<>(ingredientListAsString, HttpStatus.OK);
    }
}