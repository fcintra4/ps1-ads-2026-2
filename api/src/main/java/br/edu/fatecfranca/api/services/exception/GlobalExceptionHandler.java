package br.edu.fatecfranca.api.services.exception;

import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.edu.fatecfranca.api.services.contract.ApiError;
import br.edu.fatecfranca.api.services.contract.Contract;
import br.edu.fatecfranca.api.services.contract.FieldError;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

        private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * Erros de validação provenientes de @Valid em @RequestBody.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Contract<Void>> handleValidation(
            MethodArgumentNotValidException exception
    ) {

        List<FieldError> errors = exception
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldError(
                        error.getField(),
                        error.getCode(),
                        error.getDefaultMessage()
                ))
                .toList();

        ApiError apiError = ApiError.validation(
                "Existem campos inválidos.",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Contract.error(apiError));
    }

    /**
     * Erros de validação provenientes de @RequestParam,
     * @PathVariable etc.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Contract<Void>> handleConstraintViolation(
            ConstraintViolationException exception
    ) {

        List<FieldError> errors = exception
                .getConstraintViolations()
                .stream()
                .map(error -> new FieldError(
                        error.getPropertyPath().toString(),
                        error.getConstraintDescriptor()
                                .getAnnotation()
                                .annotationType()
                                .getSimpleName(),
                        error.getMessage()
                ))
                .toList();

        ApiError apiError = ApiError.validation(
                "Existem parâmetros inválidos.",
                errors
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Contract.error(apiError));
    }

    /**
     * Recurso não encontrado.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Contract<Void>> handleNotFound(
            ResourceNotFoundException exception
    ) {

        ApiError error = ApiError.of(
                exception.getCode(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Contract.error(error));
    }

    /**
     * Regra de negócio violada.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Contract<Void>> handleBusiness(
            BusinessException exception
    ) {

        ApiError error = ApiError.of(
                exception.getCode(),
                exception.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(Contract.error(error));
    }

    /**
     * Erro inesperado.
     *
     * IMPORTANTE:
     * Não retornamos exception.getMessage() para o cliente.
     * Isso evita vazar detalhes internos da aplicação.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Contract<Void>> handleGeneric(
            Exception exception
    ) {

        ApiError error = ApiError.of(
                "INTERNAL_ERROR",
                "Ocorreu um erro interno. Tente novamente mais tarde."
        );

        logger.error("Unhandled exception caught by GlobalExceptionHandler", exception);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Contract.error(error));
    }
}