package br.edu.fatecfranca.api.services;

import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.repositories.CustomerRepository;
import br.edu.fatecfranca.api.repositories.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarService {

    private final CarRepository repository;
    private final CustomerRepository customerRepository;

    public CarService(CarRepository repository, CustomerRepository customerRepository) {
        this.repository = repository;
        this.customerRepository = customerRepository;
    }

    public Car create(CarRequest request) {
        return repository.save(toEntity(request));
    }

    public List<Car> findAll() {
        return repository.findAll();
    }

    public Optional<Car> findById(Long id) {
        return repository.findById(id);
    }

    public Car update(Long id, CarRequest request) {
        Car car = toEntity(request);
        car.setId(id);
        return repository.save(car);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Car toEntity(CarRequest request) {
        Car car = new Car();
        car.setBrand(request.brand());
        car.setModel(request.model());
        car.setColor(request.color());
        car.setYearManufacture(request.yearManufacture() == null ? null : request.yearManufacture().intValue());
        car.setImported(request.imported());
        car.setPlates(request.plates());
        car.setSellingDate(request.sellingDate() == null ? null : request.sellingDate().atStartOfDay());
        car.setSellingPrice(request.sellingPrice());
        car.setCustomer(request.customerId() == null ? null : customerRepository.getReferenceById(request.customerId()));
        return car;
    }
}