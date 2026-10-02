package br.edu.fatecfranca.api.patterns.adapter;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.stereotype.Component;

import br.edu.fatecfranca.api.entities.Car;

@Component
public class LegacyValuationAdapter implements VehicleValuationPort {

  private final LegacyValuationClient legacyClient;

  public LegacyValuationAdapter(LegacyValuationClient legacyClient) {
    this.legacyClient = legacyClient;
  }

  @Override
  public BigDecimal estimate(Car car) {
    String vehicleName = car.getBrand() + " " + car.getModel();
    String originCode = Boolean.TRUE.equals(car.getImported()) ? "IMP" : "NAC";
    long valueInCents = legacyClient.calculateValueInCents(
            vehicleName,
            car.getYearManufacture(),
            originCode);

    return BigDecimal.valueOf(valueInCents, 2)
            .setScale(2, RoundingMode.HALF_UP);
  }
}
