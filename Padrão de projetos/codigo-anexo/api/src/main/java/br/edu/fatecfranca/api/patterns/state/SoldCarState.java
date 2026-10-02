package br.edu.fatecfranca.api.patterns.state;

public class SoldCarState implements CarSaleState {

  @Override
  public String name() {
    return "SOLD";
  }
}
