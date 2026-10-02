package br.edu.fatecfranca.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.services.CarService;

@ExtendWith(MockitoExtension.class)
class CarControllerTests {

  @Mock
  private CarService service;

  @InjectMocks
  private CarController controller;

  @Test
  void createReturnsCreatedCar() {
    Car car = new Car();
    when(service.create(car)).thenReturn(car);

    ResponseEntity<Car> response = controller.create(car);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertSame(car, response.getBody());
  }

  @Test
  void findAllReturnsCars() {
    List<Car> cars = List.of(new Car(), new Car());
    when(service.findAll()).thenReturn(cars);

    List<Car> response = controller.findAll();

    assertSame(cars, response);
  }

  @Test
  void findByIdReturnsCarWhenItExists() {
    Car car = new Car();
    car.setId(4L);
    when(service.findById(4L)).thenReturn(Optional.of(car));

    ResponseEntity<Car> response = controller.findById(4L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertSame(car, response.getBody());
  }

  @Test
  void findByIdReturnsNotFoundWhenCarDoesNotExist() {
    when(service.findById(19L)).thenReturn(Optional.empty());

    ResponseEntity<Car> response = controller.findById(19L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateReturnsSavedCarWhenItExists() {
    Car car = new Car();
    when(service.existsById(5L)).thenReturn(true);
    when(service.update(car)).thenReturn(car);

    ResponseEntity<Car> response = controller.update(5L, car);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(5L, car.getId());
    assertSame(car, response.getBody());
    verify(service).update(car);
  }

  @Test
  void updateReturnsNotFoundWhenCarDoesNotExist() {
    Car car = new Car();
    when(service.existsById(25L)).thenReturn(false);

    ResponseEntity<Car> response = controller.update(25L, car);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(service, never()).update(car);
  }

  @Test
  void deleteReturnsNoContentWhenCarExists() {
    when(service.existsById(3L)).thenReturn(true);

    ResponseEntity<Void> response = controller.delete(3L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(service).deleteById(3L);
  }

  @Test
  void deleteReturnsNotFoundWhenCarDoesNotExist() {
    when(service.existsById(11L)).thenReturn(false);

    ResponseEntity<Void> response = controller.delete(11L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(service, never()).deleteById(11L);
  }
}
