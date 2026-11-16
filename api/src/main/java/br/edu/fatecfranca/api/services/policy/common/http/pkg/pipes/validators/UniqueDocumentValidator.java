package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.repositories.interfaces.CustomerRepository;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CustomerReq;

public class UniqueDocumentValidator extends RequestValidator<CustomerReq> {

    private final CustomerRepository repository;

    public UniqueDocumentValidator(CustomerRepository repository, RequestValidator<CustomerReq> next) {
        super(next);
        this.repository = repository;
    }

    @Override
    protected void check(CustomerReq request, Long customerId) {
        if (request == null || request.getIdentDocument() == null || request.getIdentDocument().isBlank()) {
            return;
        }

        String document = request.getIdentDocument().trim();
        boolean alreadyExists;

        if (customerId == null) {
            alreadyExists = repository.existsByIdentDocument(document);
        } else {
            alreadyExists = repository.existsByIdentDocumentAndIdNot(
                document, customerId
            );
        }

        if (alreadyExists) {
            throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "O documento informado já está cadastrado."
            );
        }
    }
}

