package br.edu.fatecfranca.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import br.edu.fatecfranca.api.services.policy.common.http.pkg.interceptors.exception.GlobalExceptionHandler;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CarReq;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void carYearAboveMaximumReturnsConflict() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new CarReq(), "carReq");
        bindingResult.addError(new FieldError(
                "carReq",
                "yearManufacture",
                2994,
                false,
                new String[] {"Max.carReq.yearManufacture"},
                new Object[] {2100},
                "must be less than or equal to 2100"
        ));

        var response = handler.handleValidation(
                new MethodArgumentNotValidException(null, bindingResult)
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getHttpStatus());
    }

    @Test
    void negativeCarSellingPriceReturnsConflict() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new CarReq(), "carReq");
        bindingResult.addError(new FieldError(
                "carReq",
                "sellingPrice",
                -400,
                false,
                new String[] {"DecimalMin.carReq.sellingPrice"},
                new Object[] {"0.00"},
                "must be greater than or equal to 0.00"
        ));

        var response = handler.handleValidation(
                new MethodArgumentNotValidException(null, bindingResult)
        );

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("INVALID_SELLING_PRICE", response.getBody().getError().getCode());
    }

    @Test
    void otherValidationErrorsRemainBadRequest() {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new CarReq(), "carReq");
        bindingResult.addError(new FieldError(
                "carReq",
                "brand",
                "",
                false,
                new String[] {"NotBlank.carReq.brand"},
                null,
                "must not be blank"
        ));

        var response = handler.handleValidation(
                new MethodArgumentNotValidException(null, bindingResult)
        );

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
