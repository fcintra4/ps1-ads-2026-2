package br.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Customers;
import br.edu.fatecfranca.api.logic.domain.CustomersDomain;
import br.edu.fatecfranca.api.repositories.interfaces.CustomerRepository;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CustomerReq;

@Service
public class CustomersService { 

    private final CustomersDomain domain;

    public CustomersService(CustomerRepository repository) { this.domain = new CustomersDomain(repository); }

    private CustomerReq returnEntityToRequest(Customers customer) {
        CustomerReq request = new CustomerReq();
        request.setName(customer.getName());
        request.setIdentDocument(customer.getIdentDocument());
        request.setBirthDate(customer.getBirthDate());
        request.setStreetName(customer.getStreetName());
        request.setHouseNumber(customer.getHouseNumber());
        request.setComplements(customer.getComplements());
        request.setDistrict(customer.getDistrict());
        request.setMunicipality(customer.getMunicipality());
        request.setState(customer.getState());
        request.setPhone(customer.getPhone());
        request.setEmail(customer.getEmail());
        return request;
    }

    public Customers create(Customers customer) {
        domain.ChainOfResponsibility(returnEntityToRequest(customer), null);
        return domain.create(customer);
    }

    public List<Customers> findAll() { return domain.findAll(); }
    public Page<Customers> findAll(Pageable pageable) { return domain.findAll(pageable); }
    public Optional<Customers> findById(Long id) { return domain.findById(id); }

    public Customers update(Customers customer) {
        domain.ChainOfResponsibility(returnEntityToRequest(customer), customer.getId());
        return domain.update(customer);
    }

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
