package br.edu.fatecfranca.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.repositories.CarRepository;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarRepository repository;

    public CarController(CarRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Car> create(@RequestBody Car car) {
        Car savedCar = repository.save(car);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedCar);
    }

    @GetMapping
    public List<Car> findAll() {
        return repository.findAll();
    }
}