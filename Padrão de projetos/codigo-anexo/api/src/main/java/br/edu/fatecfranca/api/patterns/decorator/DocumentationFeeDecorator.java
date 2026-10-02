package br.edu.fatecfranca.api.patterns.decorator;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DocumentationFeeDecorator extends SalePriceDecorator {

  private static final BigDecimal FEE = new BigDecimal("850.00");

  public DocumentationFeeDecorator(SalePrice component) {
    super(component);
  }

  @Override
  public BigDecimal total() {
    return component.total().add(FEE).setScale(2, RoundingMode.HALF_UP);
  }

  @Override
  public String description() {
    return component.description() + " + documentation fee";
  }
}
