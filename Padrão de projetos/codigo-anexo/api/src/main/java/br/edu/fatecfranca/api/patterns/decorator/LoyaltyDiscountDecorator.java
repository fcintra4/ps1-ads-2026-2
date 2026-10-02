package br.edu.fatecfranca.api.patterns.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoyaltyDiscountDecorator extends SalePriceDecorator {

  private final BigDecimal discountPercent;

  public LoyaltyDiscountDecorator(SalePrice component, BigDecimal discountPercent) {
    super(component);

    if (discountPercent == null
            || discountPercent.signum() < 0
            || discountPercent.compareTo(new BigDecimal("100")) > 0) {
      throw new IllegalArgumentException("The loyalty discount must be between 0 and 100.");
    }

    this.discountPercent = discountPercent;
  }

  @Override
  public BigDecimal total() {
    BigDecimal multiplier = BigDecimal.ONE.subtract(
            discountPercent.divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP));

    return component.total()
            .multiply(multiplier)
            .setScale(2, RoundingMode.HALF_UP);
  }

  @Override
  public String description() {
    return component.description() + " - " + discountPercent.stripTrailingZeros()
            .toPlainString() + "% loyalty discount";
  }
}
