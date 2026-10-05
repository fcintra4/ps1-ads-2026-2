package br.edu.fatecfranca.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.repositories.CustomerRepository;
import br.edu.fatecfranca.api.services.CustomerService;

class CustomerControllerTests {
    @Test
    void createAndUpdateAcceptDtoAndUseGeneratedThenPathId() throws Exception {
        CustomerRepository repository = mock(CustomerRepository.class);
        when(repository.save(any(Customer.class))).thenAnswer(invocation -> {
            Customer customer = invocation.getArgument(0);
            if (customer.getId() == null) {
                customer.setId(42L);
            }
            return customer;
        });
        when(repository.existsById(42L)).thenReturn(true);
        MockMvc mvc = MockMvcBuilders.standaloneSetup(
                new CustomerController(new CustomerService(repository))).build();
        String payload = """
                {
                  "name": "Mariana Oliveira Santos",
                  "identDocument": "52998224725",
                  "birthDate": "1995-08-17",
                  "streetName": "Rua das Acácias",
                  "houseNumber": "250",
                  "complements": "Apartamento 12",
                  "district": "Jardim América",
                  "municipality": "Franca",
                  "state": "SP",
                  "phone": "16991234567",
                  "email": "mariana.santos@example.com"
                }
                """;

        mvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.name").value("Mariana Oliveira Santos"));

        String updated = payload.replace("Rua das Acácias", "Rua das Oliveiras")
                .replace("250", "442").replace("\"Apartamento 12\"", "null");
        mvc.perform(put("/customers/42").contentType(MediaType.APPLICATION_JSON).content(updated))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(42))
                .andExpect(jsonPath("$.streetName").value("Rua das Oliveiras"))
                .andExpect(jsonPath("$.houseNumber").value("442"))
                .andExpect(jsonPath("$.complements").doesNotExist());

        mvc.perform(put("/customers/99").contentType(MediaType.APPLICATION_JSON).content(updated))
                .andExpect(status().isNotFound());
        verify(repository, times(2)).save(any(Customer.class));
    }
}
