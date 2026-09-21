package br.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Customers;
import br.edu.fatecfranca.api.repositories.interfaces.CustomerRepository;
import br.edu.fatecfranca.api.services.logic.domain.CustomersDomain;

@Service
public class CustomersService {

    private final CustomersDomain domain;

    public CustomersService(CustomerRepository repository) {
        this.domain = new CustomersDomain(repository);
    }

    public Customers create(Customers customer) { return domain.create(customer); }
    public List<Customers> findAll() { return domain.findAll(); }
    public Page<Customers> findAll(Pageable pageable) { return domain.findAll(pageable); }
    public Optional<Customers> findById(Long id) { return domain.findById(id); }
    public Customers update(Customers customer) { return domain.update(customer); }
    public boolean existsById(Long id) { return domain.existsById(id); }
    public void deleteById(Long id) { domain.deleteById(id); }

    public Optional<Customers> findByIdentDocument(String identDocument) { return domain.findByIdentDocument(identDocument); }
    public boolean existsByIdentDocument(String identDocument) { return domain.existsByIdentDocument(identDocument); }
    public Optional<Customers> findByEmail(String email) { return domain.findByEmail(email); }
    public boolean existsByEmail(String email) { return domain.existsByEmail(email); }
    public List<Customers> findByName(String name) { return domain.findByName(name); }
    public List<Customers> findByNameContainingIgnoreCase(String name) { return domain.findByNameContainingIgnoreCase(name); }
    public List<Customers> findByStateAndMunicipality(String state, String municipality) { return domain.findByStateAndMunicipality(state, municipality); }
    public List<Customers> findByState(String state) { return domain.findByState(state); }
}
