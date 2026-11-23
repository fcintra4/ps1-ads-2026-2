package br.edu.fatecfranca.api.services.policy.common.http.pkg.interceptors.exception;

import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.services.policy.common.http.pkg.errors.ApiError;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.errors.FieldError;
import br.edu.fatecfranca.api.services.policy.contracts.Contract;

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
        boolean invalidCarYear = exception.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> "yearManufacture".equals(error.getField())
                        && (hasConstraintCode(error, "Max.")
                                || hasConstraintCode(error, "Min.")));
        if (invalidCarYear) {
            Contract<Void> body = Contract.conflict(
                    "INVALID_CAR_YEAR",
                    "yearManufacture must be between 1886 and 2100."
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

        boolean negativeSellingPrice = exception.getBindingResult().getFieldErrors().stream()
                .anyMatch(error -> "sellingPrice".equals(error.getField())
                        && hasConstraintCode(error, "DecimalMin."));
        if (negativeSellingPrice) {
            Contract<Void> body = Contract.conflict(
                    "INVALID_SELLING_PRICE",
                    "sellingPrice must not be negative."
            );
            return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
        }

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

        Contract<Void> body = Contract.badRequest(apiError);
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    private boolean hasConstraintCode(org.springframework.validation.FieldError error, String prefix) {
        return error.getCodes() != null
                && Arrays.stream(error.getCodes()).anyMatch(code -> code.startsWith(prefix));
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

        Contract<Void> body = Contract.badRequest(apiError);
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Contract<Void>> handleResponseStatus(ResponseStatusException exception) {
        HttpStatus status = HttpStatus.valueOf(exception.getStatusCode().value());
        String code = status == HttpStatus.CONFLICT ? "CONFLICT" : "INVALID_REQUEST";
        String message = exception.getReason() == null ? status.getReasonPhrase() : exception.getReason();
        Contract<Void> body = Contract.error(status.value(), code, message);
        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Contract<Void>> handleUnreadableRequest(HttpMessageNotReadableException exception) {
        Contract<Void> body = Contract.badRequest(
                "MALFORMED_REQUEST",
                "Request body contains invalid JSON or a value with the wrong format."
        );
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Conflito de dados no banco (ex.: valor único duplicado).
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Contract<Void>> handleDataIntegrityViolation(
            DataIntegrityViolationException exception
    ) {
        ApiError error = ApiError.of(
                "CONFLICT",
                "Dados duplicados ou violando restrições do banco."
        );

        Contract<Void> body = Contract.conflict(error.getCode(), error.getMessage());
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
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

        Contract<Void> body = Contract.notFound(error);
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
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

        Contract<Void> body = Contract.unprocessableEntity(error);
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
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

        Contract<Void> body = Contract.internalServerError(error);
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }
}