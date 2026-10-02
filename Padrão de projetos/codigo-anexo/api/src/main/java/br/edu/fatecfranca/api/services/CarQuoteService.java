package br.edu.fatecfranca.api.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.patterns.decorator.BaseSalePrice;
import br.edu.fatecfranca.api.patterns.decorator.DocumentationFeeDecorator;
import br.edu.fatecfranca.api.patterns.decorator.ImportedVehicleFeeDecorator;
import br.edu.fatecfranca.api.patterns.decorator.LoyaltyDiscountDecorator;
import br.edu.fatecfranca.api.patterns.decorator.SalePrice;
import br.edu.fatecfranca.api.patterns.web.ResourceNotFoundException;
import br.edu.fatecfranca.api.repositories.CarRepository;

@Service
public class CarQuoteService {

  private final CarRepository repository;

  public CarQuoteService(CarRepository repository) {
    this.repository = repository;
  }

  public SalePrice quote(
          Long carId,
          BigDecimal basePrice,
          boolean includeDocumentation,
          BigDecimal loyaltyDiscountPercent) {
    Car car = repository.findById(carId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Vehicle " + carId + " was not found."));

    SalePrice price = new BaseSalePrice(basePrice);

    if (Boolean.TRUE.equals(car.getImported())) {
      price = new ImportedVehicleFeeDecorator(price);
    }

    if (includeDocumentation) {
      price = new DocumentationFeeDecorator(price);
    }

    if (loyaltyDiscountPercent != null && loyaltyDiscountPercent.signum() > 0) {
      price = new LoyaltyDiscountDecorator(price, loyaltyDiscountPercent);
    }

    return price;
  }
}
