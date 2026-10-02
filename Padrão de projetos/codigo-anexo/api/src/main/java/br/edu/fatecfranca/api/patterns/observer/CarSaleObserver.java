package br.edu.fatecfranca.api.patterns.observer;

public interface CarSaleObserver {

  void onCarSold(CarSoldEvent event);
}
