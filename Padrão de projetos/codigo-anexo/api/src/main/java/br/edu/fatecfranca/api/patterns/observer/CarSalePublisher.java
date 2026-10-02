package br.edu.fatecfranca.api.patterns.observer;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CarSalePublisher {

  private final List<CarSaleObserver> observers;

  public CarSalePublisher(List<CarSaleObserver> observers) {
    this.observers = List.copyOf(observers);
  }

  public void publish(CarSoldEvent event) {
    observers.forEach(observer -> observer.onCarSold(event));
  }
}
