package br.edu.fatecfranca.api.validators;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.dtos.CarRequest;

public class SaleDataValidator extends RequestValidator<CarRequest> {

    public SaleDataValidator(RequestValidator<CarRequest> next) {
        super(next);
    }

    @Override
    protected void check(CarRequest request, Long carId) {
        boolean hasSellingDate = request.sellingDate() != null;
        boolean hasSellingPrice = request.sellingPrice() != null;

        if (hasSellingDate != hasSellingPrice) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A data e o preço de venda devem ser informados juntos."
            );
        }

        if (hasSellingPrice && request.sellingPrice().signum() <= 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "O preço de venda deve ser maior que zero."
            );
        }
    }
}
