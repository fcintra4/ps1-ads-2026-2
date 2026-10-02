package br.edu.fatecfranca.api.patterns.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.fatecfranca.api.patterns.state.InvalidSaleStateException;

@RestControllerAdvice
public class PatternExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleNotFound(ResourceNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", exception.getMessage()));
  }

  @ExceptionHandler(InvalidSaleStateException.class)
  public ResponseEntity<Map<String, String>> handleInvalidState(
          InvalidSaleStateException exception) {
    return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", exception.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<Map<String, String>> handleInvalidArgument(
          IllegalArgumentException exception) {
    return ResponseEntity.badRequest()
            .body(Map.of("error", exception.getMessage()));
  }
}
