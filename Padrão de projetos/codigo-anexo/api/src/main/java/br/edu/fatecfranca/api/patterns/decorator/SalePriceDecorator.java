package br.edu.fatecfranca.api.patterns.decorator;

public abstract class SalePriceDecorator implements SalePrice {

  protected final SalePrice component;

  protected SalePriceDecorator(SalePrice component) {
    this.component = component;
  }
}
