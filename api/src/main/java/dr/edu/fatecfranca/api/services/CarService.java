package dr.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import dr.edu.fatecfranca.api.dtos.CarRequest;
import dr.edu.fatecfranca.api.entities.Car;
import dr.edu.fatecfranca.api.entities.Customer;
import dr.edu.fatecfranca.api.repositories.CarRepository;
import dr.edu.fatecfranca.api.repositories.CustomerRepository;
import dr.edu.fatecfranca.api.validators.RequestValidator;
import dr.edu.fatecfranca.api.validators.ManufactureYearValidator;
import dr.edu.fatecfranca.api.validators.SaleDataValidator;
import dr.edu.fatecfranca.api.validators.UniquePlatesValidator;

@Service
public class CarService {

    private final CarRepository repository;
    private final CustomerRepository customerRepository;
    private final RequestValidator<CarRequest> validator;

    public CarService(
            CarRepository repository,
            CustomerRepository customerRepository) {

        this.repository = repository;
        this.customerRepository = customerRepository;

        RequestValidator<CarRequest> platesValidator =
            new UniquePlatesValidator(repository, null);

        RequestValidator<CarRequest> saleValidator =
            new SaleDataValidator(platesValidator);

        this.validator =
            new ManufactureYearValidator(saleValidator);
    }

    private void copyToEntity(CarRequest request, Car car) {

        car.setBrand(request.brand());
        car.setModel(request.model());
        car.setColor(request.color());
        car.setYear_manufacture(request.yearManufacture());
        car.setImported(request.imported());
        car.setPlates(request.plates());
        car.setSelling_date(request.sellingDate());
        car.setSellingPrice(request.sellingPrice());

        if (request.customerId() != null) {
            Customer customer = customerRepository.findById(
                request.customerId()
            ).orElseThrow(() ->
                new IllegalArgumentException(
                    "Cliente não encontrado."
                )
            );

            car.setCustomer(customer);
        } else {
            car.setCustomer(null);
        }
    }

    public Car create(CarRequest request) {

        validator.validate(request, null);

        Car car = new Car();
        copyToEntity(request, car);

        return repository.save(car);
    }

    public List<Car> findAll() {
        return repository.findAll();
    }

    public Optional<Car> findById(Long id) {
        return repository.findById(id);
    }

    public Car update(Long id, CarRequest request) {

        validator.validate(request, id);

        Car car = repository.findById(id).orElseThrow(() ->
            new IllegalArgumentException("Carro não encontrado.")
        );

        copyToEntity(request, car);
        car.setId(id);

        return repository.save(car);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}