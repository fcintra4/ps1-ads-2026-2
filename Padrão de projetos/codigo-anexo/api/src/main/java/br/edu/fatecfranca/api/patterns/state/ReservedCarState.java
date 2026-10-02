package br.edu.fatecfranca.api.patterns.state;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.fatecfranca.api.entities.Car;

public class ReservedCarState implements CarSaleState {

  @Override
  public String name() {
    return "RESERVED";
  }

  @Override
  public void complete(Car car, BigDecimal sellingPrice, LocalDate sellingDate) {
    if (sellingPrice == null || sellingPrice.signum() <= 0) {
      throw new IllegalArgumentException("The selling price must be greater than zero.");
    }

    car.setSellingPrice(sellingPrice);
    car.setSellingDate(sellingDate);
  }

  @Override
  public void cancel(Car car) {
    car.setCustomer(null);
  }
}
