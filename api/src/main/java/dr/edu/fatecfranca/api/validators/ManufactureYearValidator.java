package dr.edu.fatecfranca.api.validators;

import java.time.Year;

import dr.edu.fatecfranca.api.dtos.CarRequest;

public class ManufactureYearValidator
        extends RequestValidator<CarRequest> {

    public ManufactureYearValidator(RequestValidator<CarRequest> next) {
        super(next);
    }

    @Override
    protected void check(CarRequest request, Long entityId) {

        int currentYear = Year.now().getValue();

        if (request.yearManufacture() < 1900
                || request.yearManufacture() > currentYear) {

            throw new IllegalArgumentException(
                "O ano de fabricação deve estar entre 1900 e " + currentYear + "."
            );
        }
    }
}