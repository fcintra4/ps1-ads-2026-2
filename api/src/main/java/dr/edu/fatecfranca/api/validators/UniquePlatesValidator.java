package dr.edu.fatecfranca.api.validators;

import dr.edu.fatecfranca.api.dtos.CarRequest;
import dr.edu.fatecfranca.api.repositories.CarRepository;

public class UniquePlatesValidator
        extends RequestValidator<CarRequest> {

    private final CarRepository repository;

    public UniquePlatesValidator(
            CarRepository repository,
            RequestValidator<CarRequest> next) {

        super(next);
        this.repository = repository;
    }

    @Override
    protected void check(CarRequest request, Long entityId) {

        boolean plateExists;

        if (entityId == null) {
            plateExists = repository.existsByPlates(request.plates());
        } else {
            plateExists = repository.existsByPlatesAndIdNot(
                request.plates(), entityId
            );
        }

        if (plateExists) {
            throw new IllegalArgumentException(
                "Já existe um carro cadastrado com essa placa."
            );
        }
    }
}
