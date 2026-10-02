package br.edu.fatecfranca.api.patterns.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ImportedVehicleFeeDecorator extends SalePriceDecorator {

  private static final BigDecimal RATE = new BigDecimal("0.03");

  public ImportedVehicleFeeDecorator(SalePrice component) {
    super(component);
  }

  @Override
  public BigDecimal total() {
    return component.total()
            .multiply(BigDecimal.ONE.add(RATE))
            .setScale(2, RoundingMode.HALF_UP);
  }

  @Override
  public String description() {
    return component.description() + " + 3% imported vehicle fee";
  }
}
