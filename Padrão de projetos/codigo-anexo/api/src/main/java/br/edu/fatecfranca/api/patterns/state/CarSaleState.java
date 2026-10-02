package br.edu.fatecfranca.api.patterns.state;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;

public interface CarSaleState {

  String name();

  default void reserve(Car car, Customer customer) {
    throw new InvalidSaleStateException(
            "A vehicle in state " + name() + " cannot be reserved.");
  }

  default void complete(Car car, BigDecimal sellingPrice, LocalDate sellingDate) {
    throw new InvalidSaleStateException(
            "A vehicle in state " + name() + " cannot be sold.");
  }

  default void cancel(Car car) {
    throw new InvalidSaleStateException(
            "A vehicle in state " + name() + " has no reservation to cancel.");
  }
}
