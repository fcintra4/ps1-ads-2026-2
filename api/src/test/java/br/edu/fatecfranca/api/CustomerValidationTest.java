package br.edu.fatecfranca.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.entities.Customers;
import br.edu.fatecfranca.api.repositories.interfaces.CustomerRepository;
import br.edu.fatecfranca.api.services.CustomersService;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CustomerReq;

@ExtendWith(MockitoExtension.class)
class CustomerValidationTest {

    @Mock
    private CustomerRepository repository;

    @Test
    void duplicateDocumentReturnsConflictWithoutSaving() {
        when(repository.existsByIdentDocument("52998224725")).thenReturn(true);
        CustomersService service = new CustomersService(repository);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.create(validCustomer().toEntity())
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
        verify(repository, never()).save(any());
    }

    @Test
    void invalidStateReturnsBadRequestWithoutSaving() {
        CustomerReq request = validCustomer();
        request.setState("XX");
        CustomersService service = new CustomersService(repository);

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> service.create(request.toEntity())
        );

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        verify(repository, never()).save(any());
    }

    private CustomerReq validCustomer() {
        CustomerReq request = new CustomerReq();
        request.setName("Ana Souza");
        request.setIdentDocument("529.982.247-25");
        request.setStreetName("Rua das Flores");
        request.setHouseNumber("123");
        request.setDistrict("Centro");
        request.setMunicipality("Franca");
        request.setState("SP");
        request.setPhone("(11) 98765-4321");
        request.setEmail("ANA@example.com");
        return request;
    }
}
