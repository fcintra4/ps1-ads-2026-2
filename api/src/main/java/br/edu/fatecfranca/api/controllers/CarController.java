package br.edu.fatecfranca.api.controllers;

import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.controllers.dtos.CarDto;
import br.edu.fatecfranca.api.entities.Cars;
import br.edu.fatecfranca.api.entities.Customers;
import br.edu.fatecfranca.api.repositories.interfaces.CarRepository;
import br.edu.fatecfranca.api.services.contract.Contract;
import br.edu.fatecfranca.api.services.contract.PageMeta;
import jakarta.validation.Valid;

@RestController @RequestMapping("/cars")
public class CarController {

    private final CarRepository __cr__;
    public CarController(CarRepository x) { this.__cr__ = x; }

    private boolean exists(Long id) { return this.__cr__.existsById(id); }

    @PostMapping
    public ResponseEntity<Contract<Long>> create(@Valid @RequestBody CarDto carDto, BindingResult br) {
        if (br.hasErrors()) {
            String msg = br.getAllErrors()
                .stream()
                .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
                .collect(Collectors.joining("; "));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Contract.badRequest("BAD_REQUEST", msg));
        }
        Contract<Long> body = Contract.created(this.__cr__.save(carDto.toEntity()).getId());
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract<Cars>> update(@PathVariable Long id, @Valid @RequestBody CarDto carDto, BindingResult br) {
        if (!exists(id)) {
            Contract<Cars> body = Contract.notFound("CAR_NOT_FOUND", "Car not found");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        if (br.hasErrors()) {
            String msg = br.getAllErrors()
                .stream()
                .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
                .collect(Collectors.joining("; "));
            Contract<Cars> body = Contract.badRequest("BAD_REQUEST", msg);
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        Cars car = carDto.toEntity();
        car.setId(id);
        Contract<Cars> body = Contract.ok(this.__cr__.save(car));
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @GetMapping
    public ResponseEntity<Contract<Iterable<Cars>>> getAll(Pageable pageable) {
        Page<Cars> page = this.__cr__.findAll(pageable);
        Contract<Iterable<Cars>> body = Contract.okPage(
            page.getContent(), PageMeta.of(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages())
        );
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract<Cars>> find(@PathVariable Long id) {
        Contract<Cars> body = this.__cr__.findById(id)
                .map(Contract::ok)
                .orElse(Contract.notFound("CAR_NOT_FOUND", "Car not found"));
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Contract<Void>> delete(@PathVariable Long id) {
        if (!exists(id)) {
            Contract<Void> body = Contract.notFound("CAR_NOT_FOUND", "Car not found");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        this.__cr__.deleteById(id);
        Contract<Void> body = Contract.noContent();
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Contract<Cars>> patch(@PathVariable Long id, @RequestBody CarDto carDto) {
        var opt = this.__cr__.findById(id);
        if (opt.isEmpty()) {
            Contract<Cars> body = Contract.notFound("CAR_NOT_FOUND", "Car not found");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        if (!carDto.isValidForUpdate()) {
            Contract<Cars> body = Contract.badRequest("BAD_REQUEST", "no fields provided for update");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }

        Cars c = opt.get();
        if (carDto.getBrand() != null && !carDto.getBrand().isBlank()) c.setBrand(carDto.getBrand());
        if (carDto.getModel() != null && !carDto.getModel().isBlank()) c.setModel(carDto.getModel());
        if (carDto.getColor() != null && !carDto.getColor().isBlank()) c.setColor(carDto.getColor());
        if (carDto.getYearManufacture() != null) c.setYearManufacture(carDto.getYearManufacture());
        if (carDto.getImported() != null) c.setImported(carDto.getImported());
        if (carDto.getPlates() != null && !carDto.getPlates().isBlank()) c.setPlates(carDto.getPlates());
        if (carDto.getSellingDate() != null) c.setSellingDate(carDto.getSellingDate());
        if (carDto.getSellingPrice() != null) c.setSellingPrice(carDto.getSellingPrice());
        if (carDto.getCustomerId() != null) {
            Customers customer = new Customers();
            customer.setId(carDto.getCustomerId());
            c.setCustomer(customer);
        }

        Contract<Cars> body = Contract.ok(this.__cr__.save(c));
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }
}
