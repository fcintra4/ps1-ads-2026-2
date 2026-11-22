package br.edu.fatecfranca.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.dtos.CustomerRequest;
import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.repositories.CustomerRepository;

class CustomerValidationTests {
    private final CustomerRepository repository = mock(CustomerRepository.class);
    private final CustomerService service = new CustomerService(repository);

    private CustomerRequest request(String state) {
        return new CustomerRequest("Marina Alves Ribeiro", "529.982.247-25", null,
                "Rua das Acácias", "245", null, "Centro", "Franca", state,
                "16999990123", "marina.ribeiro@example.com");
    }

    @Test
    void invalidStateStopsBeforeDocumentAndEmail() {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.create(request("XY")));

        assertEquals(HttpStatus.BAD_REQUEST, error.getStatusCode());
        assertEquals("A UF informada não existe.", error.getReason());
        verifyNoInteractions(repository);
    }

    @Test
    void duplicateDocumentStopsBeforeEmailAndSave() {
        when(repository.existsByIdentDocument("529.982.247-25")).thenReturn(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.create(request("SP")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        assertEquals("O documento informado já está cadastrado.", error.getReason());
        verify(repository, never()).existsByEmail(any());
        verify(repository, never()).save(any());
    }

    @Test
    void duplicateEmailStopsBeforeSave() {
        when(repository.existsByEmail("marina.ribeiro@example.com")).thenReturn(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.create(request("SP")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        assertEquals("O e-mail informado já está cadastrado.", error.getReason());
        var order = inOrder(repository);
        order.verify(repository).existsByIdentDocument("529.982.247-25");
        order.verify(repository).existsByEmail("marina.ribeiro@example.com");
        verify(repository, never()).save(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
            "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN",
            "RS", "RO", "RR", "SC", "SP", "SE", "TO"})
    void acceptsEveryBrazilianState(String state) {
        when(repository.save(any(Customer.class))).thenAnswer(call -> call.getArgument(0));

        assertEquals(state, service.create(request(state)).getState());
    }

    @Test
    void updateExcludesOwnIdFromBothUniquenessQueries() {
        when(repository.save(any(Customer.class))).thenAnswer(call -> call.getArgument(0));

        Customer saved = service.update(7L, request("SP"));

        assertEquals(7L, saved.getId());
        var order = inOrder(repository);
        order.verify(repository).existsByIdentDocumentAndIdNot("529.982.247-25", 7L);
        order.verify(repository).existsByEmailAndIdNot("marina.ribeiro@example.com", 7L);
        order.verify(repository).save(any(Customer.class));
        verify(repository, never()).existsByIdentDocument(any());
        verify(repository, never()).existsByEmail(any());
    }

    @Test
    void updateRejectsDocumentUsedByAnotherCustomer() {
        when(repository.existsByIdentDocumentAndIdNot("529.982.247-25", 7L)).thenReturn(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.update(7L, request("SP")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).existsByEmailAndIdNot(any(), any());
        verify(repository, never()).save(any());
    }

    @Test
    void updateRejectsEmailUsedByAnotherCustomer() {
        when(repository.existsByEmailAndIdNot("marina.ribeiro@example.com", 7L)).thenReturn(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.update(7L, request("SP")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).save(any());
    }
}
