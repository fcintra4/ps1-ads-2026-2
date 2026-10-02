package br.edu.fatecfranca.api.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.patterns.state.CarSaleStateContext;
import br.edu.fatecfranca.api.patterns.state.InvalidSaleStateException;

class CarSaleStateContextTests {

  @Test
  void followsAvailableReservedSoldFlow() {
    Car car = new Car();
    Customer customer = new Customer();
    customer.setId(10L);

    CarSaleStateContext available = CarSaleStateContext.from(car);
    assertEquals("AVAILABLE", available.currentState());
    available.reserve(customer);

    CarSaleStateContext reserved = CarSaleStateContext.from(car);
    assertEquals("RESERVED", reserved.currentState());
    reserved.complete(new BigDecimal("78000.00"));

    assertEquals("SOLD", CarSaleStateContext.from(car).currentState());
    assertEquals(new BigDecimal("78000.00"), car.getSellingPrice());
  }

  @Test
  void doesNotAllowCompletingAnAvailableVehicle() {
    Car car = new Car();

    assertThrows(
            InvalidSaleStateException.class,
            () -> CarSaleStateContext.from(car).complete(new BigDecimal("50000.00")));
  }
}
