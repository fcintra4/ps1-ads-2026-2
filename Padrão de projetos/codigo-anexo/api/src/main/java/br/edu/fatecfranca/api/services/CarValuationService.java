package br.edu.fatecfranca.api.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.patterns.adapter.VehicleValuationPort;
import br.edu.fatecfranca.api.patterns.web.ResourceNotFoundException;
import br.edu.fatecfranca.api.repositories.CarRepository;

@Service
public class CarValuationService {

  private final CarRepository repository;
  private final VehicleValuationPort valuationPort;

  public CarValuationService(
          CarRepository repository,
          VehicleValuationPort valuationPort) {
    this.repository = repository;
    this.valuationPort = valuationPort;
  }

  public BigDecimal estimate(Long carId) {
    Car car = repository.findById(carId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Vehicle " + carId + " was not found."));

    return valuationPort.estimate(car);
  }
}
