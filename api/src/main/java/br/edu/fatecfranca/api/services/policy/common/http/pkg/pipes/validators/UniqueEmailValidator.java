package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.repositories.interfaces.CustomerRepository;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CustomerReq;

public class UniqueEmailValidator extends RequestValidator<CustomerReq> {

    private final CustomerRepository repository;

    public UniqueEmailValidator(CustomerRepository repository, RequestValidator<CustomerReq> next) {
        super(next);
        this.repository = repository;
    }

    @Override
    protected void check(CustomerReq request, Long customerId) {
        if (request == null || request.getEmail() == null || request.getEmail().isBlank()) {
            return;
        }

        String email = request.getEmail().trim();
        boolean alreadyExists;

        if (customerId == null) {
            alreadyExists = repository.existsByEmail(email);
        } else {
            alreadyExists = repository.existsByEmailAndIdNot(email, customerId);
        }

        if (alreadyExists) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "O e-mail informado já está cadastrado."
            );
        }
    }
}

