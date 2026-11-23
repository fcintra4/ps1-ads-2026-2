package br.edu.fatecfranca.api.validators;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.dtos.CarRequest;

public class SaleDataValidator extends RequestValidator<CarRequest> {

    public SaleDataValidator(RequestValidator<CarRequest> next) {
        super(next);
    }

    @Override
    protected void check(CarRequest request, Long carId) {
        boolean hasDate = request.sellingDate() != null;
        boolean hasPrice = request.sellingPrice() != null;

        // Data e preço devem vir juntos, ou ambos ausentes (carro à venda)
        if (hasDate != hasPrice) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A data de venda e o preço de venda devem ser informados juntos.");
        }

        if (hasPrice && request.sellingPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "O preço de venda deve ser maior que zero.");
        }
    }
}
