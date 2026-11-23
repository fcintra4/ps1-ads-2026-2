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

       // Regra aplicada somente se a placa foi informada
       if (plates == null || plates.isBlank()) {
           return;
       }

       boolean alreadyExists;

       if (carId == null) {
           alreadyExists = repository.existsByPlates(plates);
       } else {
           // No PUT, desconsidera o próprio carro
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