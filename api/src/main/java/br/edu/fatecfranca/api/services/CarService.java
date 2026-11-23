package br.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.repositories.CarRepository;
import br.edu.fatecfranca.api.repositories.CustomerRepository;
import br.edu.fatecfranca.api.validators.ManufactureYearValidator;
import br.edu.fatecfranca.api.validators.RequestValidator;
import br.edu.fatecfranca.api.validators.SaleDataValidator;
import br.edu.fatecfranca.api.validators.UniquePlatesValidator;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final RequestValidator<CarRequest> validator;

    public CarService(CarRepository carRepository, CustomerRepository customerRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;

        RequestValidator<CarRequest> platesValidator =
            new UniquePlatesValidator(carRepository, null);

        RequestValidator<CarRequest> saleValidator =
            new SaleDataValidator(platesValidator);

        this.validator = new ManufactureYearValidator(saleValidator);
    }

    private void copyToEntity(CarRequest request, Car car) {
        car.setBrand(request.brand());
        car.setModel(request.model());
        car.setColor(request.color());
        car.setYearManufacture(request.yearManufacture());
        car.setImported(request.imported());
        car.setPlates(request.plates());
        car.setSellingDate(request.sellingDate());
        car.setSellingPrice(request.sellingPrice());

        if (request.customerId() != null) {
            Customer customer = customerRepository.findById(request.customerId()).orElse(null);
            car.setCustomer(customer);
        } else {
            car.setCustomer(null);
        }
    }

    public List<Car> findAll() {
        return carRepository.findAll();
    }

    public Optional<Car> findById(Long id) {
        return carRepository.findById(id);
    }

    public Car create(CarRequest request) {
        validator.validate(request, null);

        Car car = new Car();
        copyToEntity(request, car);
        return carRepository.save(car);
    }

    public Car update(Long id, CarRequest request) {
        validator.validate(request, id);

        Car car = new Car();
        copyToEntity(request, car);
        car.setId(id);
        return carRepository.save(car);
    }

    public boolean existsById(Long id) {
        return carRepository.existsById(id);
    }

    public void deleteById(Long id) {
        carRepository.deleteById(id);
    }
}