package br.edu.fatecfranca.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import br.edu.fatecfranca.api.controllers.CarController;
import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.services.CarService;
import br.edu.fatecfranca.api.validators.ManufactureYearValidator;
import br.edu.fatecfranca.api.validators.SaleDataValidator;

class CarStatusTest {

    @Test
    void invalidManufactureYearReturnsConflict() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new ManufactureYearValidator(null)
                        .validate(carRequest(2994L, null, null), null)
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void incompleteSaleDataReturnsConflict() {
        SaleDataValidator validator = new SaleDataValidator(null);

        ResponseStatusException dateWithoutPrice = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(
                        carRequest(1994L, LocalDate.of(2026, 11, 23), null),
                        null
                )
        );
        ResponseStatusException priceWithoutDate = assertThrows(
                ResponseStatusException.class,
                () -> validator.validate(
                        carRequest(1994L, null, new BigDecimal("13500")),
                        null
                )
        );

        assertEquals(HttpStatus.CONFLICT, dateWithoutPrice.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, priceWithoutDate.getStatusCode());
    }

    @Test
    void negativeSalePriceReturnsConflict() {
        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> new SaleDataValidator(null)
                        .validate(
                                carRequest(1994L, LocalDate.of(2026, 11, 23), new BigDecimal("-400")),
                                null
                        )
        );

        assertEquals(HttpStatus.CONFLICT, exception.getStatusCode());
    }

    @Test
    void successfulCarCreationReturnsOk() {
        CarService service = mock(CarService.class);
        CarRequest request = carRequest(1994L, LocalDate.of(2026, 11, 23), new BigDecimal("13500"));
        Car savedCar = new Car();
        when(service.create(request)).thenReturn(savedCar);

        var response = new CarController(service).create(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(savedCar, response.getBody());
    }

    private CarRequest carRequest(Long year, LocalDate sellingDate, BigDecimal sellingPrice) {
        return new CarRequest(
                "Volkswagen",
                "Gol GTI",
                "Azul",
                year,
                false,
                "KWY9J35",
                sellingDate,
                sellingPrice,
                null
        );
    }
}
