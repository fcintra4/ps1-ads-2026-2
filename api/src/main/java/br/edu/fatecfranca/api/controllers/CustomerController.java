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

import br.edu.fatecfranca.api.controllers.dtos.CustomerDto;
import br.edu.fatecfranca.api.entities.Customers;
import br.edu.fatecfranca.api.repositories.interfaces.CustomerRepository;
import br.edu.fatecfranca.api.services.contract.Contract;
import br.edu.fatecfranca.api.services.contract.PageMeta;
import jakarta.validation.Valid;

@RestController @RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository __cr__;
    public CustomerController(CustomerRepository x) { this.__cr__ = x; }

    private boolean exists(Long id) { return this.__cr__.existsById(id); }

    @PostMapping
    public ResponseEntity<Contract<Long>> create(@Valid @RequestBody CustomerDto customerDto, BindingResult br) {
        if (br.hasErrors()) {
            String msg = br.getAllErrors()
            .stream()
            .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
            .collect(Collectors.joining("; "));
            Contract<Long> body = Contract.badRequest("BAD_REQUEST", msg);
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        Contract<Long> body = Contract.created(this.__cr__.save(customerDto.toEntity()).getId());
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Contract<Customers>> update(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto, BindingResult br) {
        if (!exists(id)) {
            Contract<Customers> body = Contract.notFound("CUSTOMER_NOT_FOUND", "Customer not found");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        if (br.hasErrors()) {
            String msg = br.getAllErrors()
            .stream()
            .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
            .collect(Collectors.joining("; "));
            Contract<Customers> body = Contract.badRequest("BAD_REQUEST", msg);
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        customerDto.toEntity().setId(id);
        Contract<Customers> body = Contract.ok(this.__cr__.save(customerDto.toEntity()));
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @GetMapping
    public ResponseEntity<Contract<Iterable<Customers>>> getAll(Pageable pageable) {
        Page<Customers> page = this.__cr__.findAll(pageable);
        Contract<Iterable<Customers>> body = Contract.okPage(
            page.getContent(), PageMeta.of(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages())
        );
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Contract<Customers>> find(@PathVariable Long id) {
        Contract<Customers> body = this.__cr__.findById(id)
                .map(Contract::ok)
                .orElse(Contract.notFound("CUSTOMER_NOT_FOUND", "Customer not found"));
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Contract<Void>> delete(@PathVariable Long id) {
        if (!exists(id)) {
            Contract<Void> body = Contract.notFound("CUSTOMER_NOT_FOUND", "Customer not found");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        this.__cr__.deleteById(id);
        Contract<Void> body = Contract.noContent();
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Contract<Customers>> patch(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        var opt = this.__cr__.findById(id);
        if (opt.isEmpty()) {
            Contract<Customers> body = Contract.notFound("CUSTOMER_NOT_FOUND", "Customer not found");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        if (!customerDto.isValidForUpdate()) {
            Contract<Customers> body = Contract.badRequest("BAD_REQUEST", "no fields provided for update");
            return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
        }
        Customers c = opt.get();
        if (customerDto.getName() != null && !customerDto.getName().isBlank()) c.setName(customerDto.getName());
        if (customerDto.getIdentDocument() != null && !customerDto.getIdentDocument().isBlank()) c.setIdentDocument(customerDto.getIdentDocument());
        if (customerDto.getBirthDate() != null) c.setBirthDate(customerDto.getBirthDate());
        if (customerDto.getStreetName() != null && !customerDto.getStreetName().isBlank()) c.setStreetName(customerDto.getStreetName());
        if (customerDto.getHouseNumber() != null && !customerDto.getHouseNumber().isBlank()) c.setHouseNumber(customerDto.getHouseNumber());
        if (customerDto.getComplements() != null) c.setComplements(customerDto.getComplements());
        if (customerDto.getDistrict() != null && !customerDto.getDistrict().isBlank()) c.setDistrict(customerDto.getDistrict());
        if (customerDto.getMunicipality() != null && !customerDto.getMunicipality().isBlank()) c.setMunicipality(customerDto.getMunicipality());
        if (customerDto.getState() != null && !customerDto.getState().isBlank()) c.setState(customerDto.getState());
        if (customerDto.getPhone() != null && !customerDto.getPhone().isBlank()) c.setPhone(customerDto.getPhone());
        if (customerDto.getEmail() != null && !customerDto.getEmail().isBlank()) c.setEmail(customerDto.getEmail());
        Contract<Customers> body = Contract.ok(this.__cr__.save(c));
        return ResponseEntity.status(HttpStatus.valueOf(body.getHttpStatus())).body(body);
    }
}
