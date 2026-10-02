package br.edu.fatecfranca.api.patterns.state;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;

public class CarSaleStateContext {

  private final Car car;
  private final CarSaleState state;

  private CarSaleStateContext(Car car, CarSaleState state) {
    this.car = car;
    this.state = state;
  }

  public static CarSaleStateContext from(Car car) {
    if (car.getSellingDate() != null || car.getSellingPrice() != null) {
      return new CarSaleStateContext(car, new SoldCarState());
    }

    if (car.getCustomer() != null) {
      return new CarSaleStateContext(car, new ReservedCarState());
    }

    return new CarSaleStateContext(car, new AvailableCarState());
  }

  public String currentState() {
    return state.name();
  }

  public void reserve(Customer customer) {
    state.reserve(car, customer);
  }

  public void complete(BigDecimal sellingPrice) {
    state.complete(car, sellingPrice, LocalDate.now());
  }

  public void cancel() {
    state.cancel(car);
  }
}
