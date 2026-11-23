package br.edu.fatecfranca.api.validators;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import br.edu.fatecfranca.api.dtos.CarRequest;

public class ManufactureYearValidator extends RequestValidator<CarRequest> {

    public ManufactureYearValidator(RequestValidator<CarRequest> next) {
        super(next);
    }

    @Override
    protected void check(CarRequest request, Long carId) {
        if (request.yearManufacture() != null) {
            int currentYear = LocalDate.now().getYear();
            if (request.yearManufacture() < 1900 || request.yearManufacture() > currentYear) {
                throw new ResponseStatusException(
                    HttpStatus.CONFLICT, // Mudou para 409
                    "O ano de fabricação deve estar entre 1900 e o ano atual."
                );
            }
        }
    }
}