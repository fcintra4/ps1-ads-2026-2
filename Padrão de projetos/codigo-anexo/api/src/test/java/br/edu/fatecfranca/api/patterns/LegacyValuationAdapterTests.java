package br.edu.fatecfranca.api.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.patterns.adapter.LegacyValuationAdapter;
import br.edu.fatecfranca.api.patterns.adapter.LegacyValuationClient;

class LegacyValuationAdapterTests {

  @Test
  void convertsLegacyCentsToApplicationMoneyFormat() {
    Car car = new Car();
    car.setBrand("Nissan");
    car.setModel("Versa");
    car.setYearManufacture(2021);
    car.setImported(false);
    LegacyValuationAdapter adapter = new LegacyValuationAdapter(new LegacyValuationClient());

    BigDecimal value = adapter.estimate(car);

    assertEquals(2, value.scale());
    assertTrue(value.signum() > 0);
  }
}
