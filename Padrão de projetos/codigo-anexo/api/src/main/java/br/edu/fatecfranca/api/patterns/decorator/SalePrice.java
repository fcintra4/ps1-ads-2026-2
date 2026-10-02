package br.edu.fatecfranca.api.patterns.decorator;

import java.math.BigDecimal;

public interface SalePrice {

  BigDecimal total();

  String description();
}
