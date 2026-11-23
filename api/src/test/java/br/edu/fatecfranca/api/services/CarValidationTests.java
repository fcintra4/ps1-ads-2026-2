package br.edu.fatecfranca.api.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Year;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.repositories.CarRepository;
import br.edu.fatecfranca.api.repositories.CustomerRepository;

class CarValidationTests {
    private final CarRepository repository = mock(CarRepository.class);
    private final CustomerRepository customers = mock(CustomerRepository.class);
    private final CarService service = new CarService(repository, customers);

    private CarRequest request(Long year, LocalDate date, BigDecimal price, String plates) {
        return new CarRequest("Honda", "Civic", "Preto", year, false, plates, date, price, null);
    }

    static Stream<Long> invalidYears() {
        return Stream.of(1899L, 0L, -1L, (long) Year.now().getValue() + 1, Long.MAX_VALUE);
    }

    @ParameterizedTest
    @MethodSource("invalidYears")
    void invalidYearStopsChainBeforeSaleDataAndPlates(Long year) {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.create(request(year, LocalDate.now(), null, "ABC1D23")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        assertEquals("O ano de fabricação deve estar entre 1900 e o ano atual.", error.getReason());
        verifyNoInteractions(repository, customers);
    }

    static Stream<Arguments> validYears() {
        return Stream.of(Arguments.of((Long) null), Arguments.of(1900L),
                Arguments.of((long) Year.now().getValue()));
    }

    @ParameterizedTest
    @MethodSource("validYears")
    void acceptsYearLimitsAndAbsentYear(Long year) {
        when(repository.save(any(Car.class))).thenAnswer(call -> call.getArgument(0));

        Car saved = service.create(request(year, null, null, "ABC1D23"));

        assertEquals(year == null ? null : year.intValue(), saved.getYearManufacture());
        verify(repository).existsByPlates("ABC1D23");
        verify(repository).save(any(Car.class));
    }

    static Stream<Arguments> invalidSaleData() {
        return Stream.of(
                Arguments.of(LocalDate.now(), null),
                Arguments.of(null, new BigDecimal("100.00")),
                Arguments.of(LocalDate.now(), BigDecimal.ZERO),
                Arguments.of(LocalDate.now(), new BigDecimal("-0.01")));
    }

    @ParameterizedTest
    @MethodSource("invalidSaleData")
    void invalidSaleDataStopsBeforeCheckingPlates(LocalDate date, BigDecimal price) {
        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.create(request(2021L, date, price, "ABC1D23")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verifyNoInteractions(repository, customers);
    }

    @Test
    void acceptsDateAndPositivePriceTogether() {
        when(repository.save(any(Car.class))).thenAnswer(call -> call.getArgument(0));

        Car saved = service.create(request(2021L, LocalDate.now(), new BigDecimal("0.01"), "ABC1D23"));

        assertEquals(LocalDate.now(), saved.getSellingDate());
        assertEquals(new BigDecimal("0.01"), saved.getSellingPrice());
    }

    @Test
    void duplicatePlatesOnCreateReturnConflictWithoutSaving() {
        when(repository.existsByPlates("ABC1D23")).thenReturn(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.create(request(2021L, null, null, "ABC1D23")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).save(any());
        verify(repository, never()).existsByPlatesAndIdNot(any(), any());
    }

    @Test
    void duplicatePlatesOnUpdateExcludePathId() {
        when(repository.existsByPlatesAndIdNot("ABC1D23", 7L)).thenReturn(true);

        ResponseStatusException error = assertThrows(ResponseStatusException.class,
                () -> service.update(7L, request(2021L, null, null, "ABC1D23")));

        assertEquals(HttpStatus.CONFLICT, error.getStatusCode());
        verify(repository, never()).save(any());
        verify(repository, never()).existsByPlates(any());
    }

    @Test
    void updateKeepsOwnPlatesAndUsesPathId() {
        when(repository.save(any(Car.class))).thenAnswer(call -> call.getArgument(0));

        Car saved = service.update(7L, request(2021L, null, null, "ABC1D23"));

        assertEquals(7L, saved.getId());
        verify(repository).existsByPlatesAndIdNot("ABC1D23", 7L);
        verify(repository, never()).existsByPlates(any());
    }

    @Test
    void absentPlatesSkipUniquenessQuery() {
        when(repository.save(any(Car.class))).thenAnswer(call -> call.getArgument(0));

        service.create(request(2021L, null, null, null));

        verify(repository, never()).existsByPlates(any());
        verify(repository, never()).existsByPlatesAndIdNot(any(), any());
        verify(repository).save(any(Car.class));
    }

    @Test
    void updateRunsYearAndSaleValidatorsBeforeRepository() {
        assertThrows(ResponseStatusException.class,
                () -> service.update(7L, request(1899L, null, null, "ABC1D23")));
        assertThrows(ResponseStatusException.class,
                () -> service.update(7L, request(2021L, LocalDate.now(), null, "ABC1D23")));

        verifyNoInteractions(repository, customers);
    }
}
