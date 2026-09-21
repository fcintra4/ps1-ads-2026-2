package br.edu.fatecfranca.api.services.logic.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.fatecfranca.api.entities.Cars;
import br.edu.fatecfranca.api.repositories.interfaces.CarRepository;

public class CarsDomain {

    private final CarRepository repository;

    public CarsDomain(CarRepository repository) { this.repository = repository; }

    public Cars create(Cars car) { return repository.save(car); }
    public List<Cars> findAll() { return repository.findAll(); }
    public Page<Cars> findAll(Pageable pageable) { return repository.findAll(pageable); }
    public Optional<Cars> findById(Long id) { return repository.findById(id); }
    public Cars update(Cars car) { return repository.save(car); }
    public boolean existsById(Long id) { return repository.existsById(id); }
    public void deleteById(Long id) { repository.deleteById(id); }

    public Optional<Cars> findByPlates(String plates) { return repository.findByPlates(plates); }
    public boolean existsByPlates(String plates) { return repository.existsByPlates(plates); }
    public List<Cars> findByCustomerId(Long customerId) { return repository.findByCustomerId(customerId); }
    public List<Cars> findByBrandAndModel(String brand, String model) { return repository.findByBrandAndModel(brand, model); }
    public List<Cars> findByBrand(String brand) { return repository.findByBrand(brand); }
    public List<Cars> findBySellingDate(LocalDate sellingDate) { return repository.findBySellingDate(sellingDate); }
    public List<Cars> findBySellingDateBetween(LocalDate startDate, LocalDate endDate) { return repository.findBySellingDateBetween(startDate, endDate); }
}
