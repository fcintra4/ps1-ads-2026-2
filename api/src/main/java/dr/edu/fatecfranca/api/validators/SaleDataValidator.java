package dr.edu.fatecfranca.api.validators;

import dr.edu.fatecfranca.api.dtos.CarRequest;

public class SaleDataValidator
        extends RequestValidator<CarRequest> {

    public SaleDataValidator(RequestValidator<CarRequest> next) {
        super(next);
    }
    @Override
    protected void check(CarRequest request, Long entityId) {

        boolean hasDate = request.sellingDate() != null;
        boolean hasPrice = request.sellingPrice() != null;

        if (hasDate != hasPrice) {
            throw new IllegalArgumentException(
                "A data e o preço de venda devem ser informados juntos."
            );
        }
        if (hasPrice && request.sellingPrice().signum() <= 0) {
            throw new IllegalArgumentException(
                "O preço de venda deve ser maior que zero."
            );
        }
    }
}
