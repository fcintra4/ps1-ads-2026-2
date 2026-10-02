package br.edu.fatecfranca.api.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.patterns.observer.CarSalePublisher;
import br.edu.fatecfranca.api.patterns.observer.CarSoldEvent;
import br.edu.fatecfranca.api.patterns.state.CarSaleStateContext;
import br.edu.fatecfranca.api.patterns.web.ResourceNotFoundException;
import br.edu.fatecfranca.api.repositories.CarRepository;
import br.edu.fatecfranca.api.repositories.CustomerRepository;

@Service
public class CarSaleService {

  private final CarRepository carRepository;
  private final CustomerRepository customerRepository;
  private final CarSalePublisher publisher;

  public CarSaleService(
          CarRepository carRepository,
          CustomerRepository customerRepository,
          CarSalePublisher publisher) {
    this.carRepository = carRepository;
    this.customerRepository = customerRepository;
    this.publisher = publisher;
  }

  public Car reserve(Long carId, Long customerId) {
    Car car = findCar(carId);
    Customer customer = customerRepository.findById(customerId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Customer " + customerId + " was not found."));

    CarSaleStateContext.from(car).reserve(customer);
    return carRepository.save(car);
  }

  public Car complete(Long carId, BigDecimal sellingPrice) {
    Car car = findCar(carId);
    CarSaleStateContext.from(car).complete(sellingPrice);
    Car savedCar = carRepository.save(car);

    Customer customer = savedCar.getCustomer();
    publisher.publish(new CarSoldEvent(
            savedCar.getId(),
            savedCar.getPlates(),
            customer.getId(),
            customer.getEmail(),
            savedCar.getSellingPrice(),
            savedCar.getSellingDate()));

    return savedCar;
  }

  public Car cancelReservation(Long carId) {
    Car car = findCar(carId);
    CarSaleStateContext.from(car).cancel();
    return carRepository.save(car);
  }

  public String currentState(Long carId) {
    return CarSaleStateContext.from(findCar(carId)).currentState();
  }

  private Car findCar(Long carId) {
    return carRepository.findById(carId)
            .orElseThrow(() -> new ResourceNotFoundException(
                    "Vehicle " + carId + " was not found."));
  }
}
