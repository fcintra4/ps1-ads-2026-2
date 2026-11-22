package br.edu.fatecfranca.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.repositories.CarRepository;
import br.edu.fatecfranca.api.repositories.CustomerRepository;

class CarServiceTests {
    private final CarRepository cars = mock(CarRepository.class);
    private final CustomerRepository customers = mock(CustomerRepository.class);
    private final CarService service = new CarService(cars, customers);

    private CarRequest request(Long customerId) {
        return new CarRequest("Honda", "Civic Touring", "Preto", 2021L, false,
                "DEF4G56", LocalDate.of(2026, 9, 18), new BigDecimal("99500"), customerId);
    }

    @Test
    void createResolvesCustomerAndLeavesIdForDatabase() {
        Customer customer = new Customer();
        customer.setId(7L);
        when(customers.findById(7L)).thenReturn(Optional.of(customer));
        when(cars.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Car saved = service.create(request(7L));

        assertNull(saved.getId());
        assertEquals(7L, saved.getCustomerId());
        assertEquals(2021, saved.getYearManufacture());
        assertEquals(new BigDecimal("99500"), saved.getSellingPrice());
    }

    @Test
    void updateUsesPathIdAndAllowsNoCustomer() {
        when(cars.save(any(Car.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Car saved = service.update(12L, request(null));

        assertEquals(12L, saved.getId());
        assertNull(saved.getCustomerId());
        verifyNoInteractions(customers);
    }

    @Test
    void missingCustomerDoesNotSaveCar() {
        when(customers.findById(99L)).thenReturn(Optional.empty());

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.create(request(99L)));

        assertEquals(HttpStatus.NOT_FOUND, error.getStatusCode());
        verify(cars, never()).save(any());
    }
}
