package br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.validators;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CustomerReq;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.RequestValidator;

public class StateValidator extends RequestValidator<CustomerReq> {

    private static final Set<String> VALID_STATES = Set.of(
        "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
        "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
        "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
    );

    public StateValidator(RequestValidator<CustomerReq> next) { super(next); }

    @Override
    protected void check(CustomerReq request, Long customerId) {
        if (request == null || request.getState() == null) {
            return;
        }

        if (!VALID_STATES.contains(request.getState().trim().toUpperCase())) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "A UF informada não existe."
            );
        }
    }
}

