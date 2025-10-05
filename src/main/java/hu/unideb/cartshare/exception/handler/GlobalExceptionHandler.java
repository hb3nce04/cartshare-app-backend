package hu.unideb.cartshare.exception.handler;

import hu.unideb.cartshare.dto.ApiErrorDto;
import hu.unideb.cartshare.exception.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    public ResponseEntity<Map<String,String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        Map<String,String> errors = new HashMap<>();
        exc.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(
            EntityNotFoundException.class
    )
    public ResponseEntity<ApiErrorDto> handleEntityNotFoundException(EntityNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiErrorDto.builder().message(exc.getMessage()).build());
    }
}
