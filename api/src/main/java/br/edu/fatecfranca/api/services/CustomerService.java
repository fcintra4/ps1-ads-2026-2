package br.edu.fatecfranca.api.services;

import br.edu.fatecfranca.api.dtos.CustomerRequest;
import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.repositories.CustomerRepository;
import br.edu.fatecfranca.api.validators.RequestValidator;
import br.edu.fatecfranca.api.validators.StateValidator;
import br.edu.fatecfranca.api.validators.UniqueDocumentValidator;
import br.edu.fatecfranca.api.validators.UniqueEmailValidator;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository repository;
    private final RequestValidator<CustomerRequest> validator;

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;

        RequestValidator<CustomerRequest> emailValidator =
            new UniqueEmailValidator(repository, null);

        RequestValidator<CustomerRequest> documentValidator =
            new UniqueDocumentValidator(repository, emailValidator);

        this.validator = new StateValidator(documentValidator);
    }

    public Customer create(CustomerRequest request) {
        validator.validate(request, null);

        return repository.save(toEntity(request));
    }

    public List<Customer> findAll() {
        return repository.findAll();
    }

    public Optional<Customer> findById(Long id) {
        return repository.findById(id);
    }

    public Customer update(Long id, CustomerRequest request) {
        validator.validate(request, id);

        Customer customer = toEntity(request);
        customer.setId(id);
        return repository.save(customer);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private Customer toEntity(CustomerRequest request) {
        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setIdentDocument(request.identDocument());
        customer.setBirthDate(request.birthDate());
        customer.setStreetName(request.streetName());
        customer.setHouseNumber(request.houseNumber());
        customer.setComplements(request.complements());
        customer.setDistrict(request.district());
        customer.setMunicipality(request.municipality());
        customer.setState(request.state());
        customer.setPhone(request.phone());
        customer.setEmail(request.email());
        return customer;
    }
}