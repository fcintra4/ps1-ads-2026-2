package br.edu.fatecfranca.api.logic.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.fatecfranca.api.entities.Customers;
import br.edu.fatecfranca.api.repositories.interfaces.CustomerRepository;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CustomerReq;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.RequestValidator;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators.StateValidator;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators.UniqueDocumentValidator;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators.UniqueEmailValidator;

public class CustomersDomain {

    private final CustomerRepository repository;
    private final RequestValidator<CustomerReq> validator;

    public CustomersDomain(CustomerRepository repository) {
        this.repository = repository;
        this.validator = new StateValidator(
            new UniqueDocumentValidator(repository,
                new UniqueEmailValidator(repository, null)
            )
        );
    }

    public CustomerReq ChainOfResponsibility(CustomerReq request, Long customerId) {
        validator.validate(request, customerId);
        return request;
    }

    public Customers create(Customers customer) { return repository.save(customer); }
    public List<Customers> findAll() { return repository.findAll(); }
    public Page<Customers> findAll(Pageable pageable) { return repository.findAll(pageable); }
    public Optional<Customers> findById(Long id) { return repository.findById(id); }
    public Customers update(Customers customer) { return repository.save(customer); }
    public boolean existsById(Long id) { return repository.existsById(id); }
    public void deleteById(Long id) { repository.deleteById(id); }

    public Optional<Customers> findByIdentDocument(String identDocument) { return repository.findByIdentDocument(identDocument); }
    public boolean existsByIdentDocument(String identDocument) { return repository.existsByIdentDocument(identDocument); }
    public Optional<Customers> findByEmail(String email) { return repository.findByEmail(email); }
    public boolean existsByEmail(String email) { return repository.existsByEmail(email); }
    public List<Customers> findByName(String name) { return repository.findByName(name); }
    public List<Customers> findByNameContainingIgnoreCase(String name) { return repository.findByNameContainingIgnoreCase(name); }
    public List<Customers> findByStateAndMunicipality(String state, String municipality) { return repository.findByStateAndMunicipality(state, municipality); }
    public List<Customers> findByState(String state) { return repository.findByState(state); }
}
