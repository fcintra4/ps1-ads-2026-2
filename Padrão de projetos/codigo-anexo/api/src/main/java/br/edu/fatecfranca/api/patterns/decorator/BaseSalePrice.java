package br.edu.fatecfranca.api.patterns.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BaseSalePrice implements SalePrice {

  private final BigDecimal basePrice;

  public BaseSalePrice(BigDecimal basePrice) {
    if (basePrice == null || basePrice.signum() <= 0) {
      throw new IllegalArgumentException("The base price must be greater than zero.");
    }

    this.basePrice = basePrice.setScale(2, RoundingMode.HALF_UP);
  }

  @Override
  public BigDecimal total() {
    return basePrice;
  }

  @Override
  public String description() {
    return "base price";
  }
}
