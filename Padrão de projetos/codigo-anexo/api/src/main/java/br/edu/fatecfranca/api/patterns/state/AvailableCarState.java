package br.edu.fatecfranca.api.patterns.state;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;

public class AvailableCarState implements CarSaleState {

  @Override
  public String name() {
    return "AVAILABLE";
  }

  @Override
  public void reserve(Car car, Customer customer) {
    if (customer == null) {
      throw new IllegalArgumentException("A customer is required to reserve a vehicle.");
    }

    car.setCustomer(customer);
  }
}
