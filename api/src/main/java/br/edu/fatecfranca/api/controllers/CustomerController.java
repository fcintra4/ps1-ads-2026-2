package br.edu.fatecfranca.api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.entities.Customers;
import br.edu.fatecfranca.api.repositories.CustomerRepository;
import br.edu.fatecfranca.api.services.contract.Contract;
import br.edu.fatecfranca.api.services.dto.CustomerDto;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import br.edu.fatecfranca.api.services.contract.PageMeta;

@RestController @RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository __cr__;
    public CustomerController(CustomerRepository x) { this.__cr__ = x; }

    private boolean exists(Long id) { return this.__cr__.existsById(id); }

    @PostMapping
    public Contract<Long> create(@Valid @RequestBody CustomerDto customerDto, BindingResult br) {
        if (br.hasErrors()) {
            String msg = br.getAllErrors()
            .stream()
            .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
            .collect(Collectors.joining("; "));
            return Contract.error("400", msg);
        }
        return Contract.ok(this.__cr__.save(customerDto.toEntity()).getId());
    }

    @PutMapping("/{id}")
    public Contract<Customers> update(@PathVariable Long id, @Valid @RequestBody CustomerDto customerDto, BindingResult br) {
        if (!exists(id)) return Contract.error("404", "Customer not found");
        if (br.hasErrors()) {
            String msg = br.getAllErrors()
            .stream()
            .map(e -> e.getDefaultMessage() == null ? e.toString() : e.getDefaultMessage())
            .collect(Collectors.joining("; "));
            return Contract.error("400", msg);
        }
        customerDto.toEntity().setId(id);
        return Contract.ok(this.__cr__.save(customerDto.toEntity()));
    }

    @GetMapping
    public Contract<Iterable<Customers>> getAll(Pageable pageable) {
        Page<Customers> page = this.__cr__.findAll(pageable);
        return Contract.okPage(
            page.getContent(), PageMeta.of(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages())
        );
    }

    @GetMapping("/{id}")
    public Contract<Customers> find(@PathVariable Long id) {
        return this.__cr__.findById(id).map(Contract::ok).orElse(Contract.error("404", "Customer not found"));
    }

    @DeleteMapping("/{id}")
    public Contract<Void> delete(@PathVariable Long id) {
        if (!exists(id)) return Contract.error("404", "Customer not found");
        this.__cr__.deleteById(id);
        return Contract.ok(null);
    }

    @PatchMapping("/{id}")
    public Contract<Customers> patch(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        var opt = this.__cr__.findById(id);
        if (opt.isEmpty())  return Contract.error("404", "Customer not found");
        if (!customerDto.isValidForUpdate()) return Contract.error("400", "no fields provided for update");
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
        return Contract.ok(this.__cr__.save(c));
    }
}
