package br.edu.fatecfranca.api.validators;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.repositories.CarRepository;

public class UniquePlatesValidator extends RequestValidator<CarRequest> {

    private final CarRepository repository;

    public UniquePlatesValidator(CarRepository repository,
                                 RequestValidator<CarRequest> next) {
        super(next);
        this.repository = repository;
    }

    @Override
    protected void check(CarRequest request, Long carId) {
        String plates = request.plates();

        // Se a placa não foi informada, não há o que validar aqui
        if (plates == null || plates.isBlank()) {
            return;
        }

        boolean alreadyExists;

        if (carId == null) {
            // POST: qualquer carro com a mesma placa é conflito
            alreadyExists = repository.existsByPlates(plates);
        } else {
            // PUT: desconsidera o próprio carro
            alreadyExists = repository.existsByPlatesAndIdNot(plates, carId);
        }

        if (alreadyExists) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "A placa informada já está cadastrada."
            );
        }
    }
}
