package br.edu.fatecfranca.api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Cars;
import br.edu.fatecfranca.api.repositories.interfaces.CarRepository;
import br.edu.fatecfranca.api.services.logic.domain.CarsDomain;

@Service
public class CarsService {

    private final CarsDomain domain;

    public CarsService(CarRepository repository) {
        this.domain = new CarsDomain(repository);
    }

    public Cars create(Cars car) { return domain.create(car); }
    public List<Cars> findAll() { return domain.findAll(); }
    public Page<Cars> findAll(Pageable pageable) { return domain.findAll(pageable); }
    public Optional<Cars> findById(Long id) { return domain.findById(id); }
    public Cars update(Cars car) { return domain.update(car); }
    public boolean existsById(Long id) { return domain.existsById(id); }
    public void deleteById(Long id) { domain.deleteById(id); }

    public Optional<Cars> findByPlates(String plates) { return domain.findByPlates(plates); }
    public boolean existsByPlates(String plates) { return domain.existsByPlates(plates); }
    public List<Cars> findByCustomerId(Long customerId) { return domain.findByCustomerId(customerId); }
    public List<Cars> findByBrandAndModel(String brand, String model) { return domain.findByBrandAndModel(brand, model); }
    public List<Cars> findByBrand(String brand) { return domain.findByBrand(brand); }
    public List<Cars> findBySellingDate(LocalDate sellingDate) { return domain.findBySellingDate(sellingDate); }
    public List<Cars> findBySellingDateBetween(LocalDate startDate, LocalDate endDate) { return domain.findBySellingDateBetween(startDate, endDate); }
}
