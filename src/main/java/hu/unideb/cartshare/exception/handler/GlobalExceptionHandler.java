package hu.unideb.cartshare.exception.handler;

import hu.unideb.cartshare.exception.BusinessLogicException;
import hu.unideb.cartshare.exception.EntityNotFoundException;
import hu.unideb.cartshare.model.dto.response.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.atomic.AtomicReference;

/**
 * It centralizes the error handling logic with unified error responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        AtomicReference<String> content = new AtomicReference<>("");
        exc.getBindingResult().getAllErrors().forEach((error) -> {
            String errorMessage = error.getDefaultMessage();
            content.set(content.get() + errorMessage + "\n");
        });
        content.set(content.get().substring(0, content.get().length() - 1));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder().title("Missing credentials").message(content.get()).build());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(EntityNotFoundException exc) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                ErrorResponseDto.builder().title("Not found").message(exc.getMessage()).build());
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponseDto> handleBusinessLogicException(BusinessLogicException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder().title("Invalid operation").message(exc.getMessage()).build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exc) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ErrorResponseDto.builder().title("Invalid request").message("Method not supported").build());
    }
}
