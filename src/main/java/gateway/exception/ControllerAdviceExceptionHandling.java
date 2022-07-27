package gateway.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerAdviceExceptionHandling extends ResponseEntityExceptionHandler {

    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<String> handleIngredientNotFoundException(JsonProcessingException jsonProcessingException) {
        return new ResponseEntity<>(jsonProcessingException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}