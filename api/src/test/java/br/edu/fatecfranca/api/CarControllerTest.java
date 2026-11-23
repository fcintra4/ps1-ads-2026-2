package br.edu.fatecfranca.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import br.edu.fatecfranca.api.controllers.CarController;
import br.edu.fatecfranca.api.entities.Cars;
import br.edu.fatecfranca.api.services.CarsService;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CarReq;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarsService carsService;

    @Test
    void createReturnsConflictWhenPlateAlreadyExists() {
        when(carsService.existsByPlates("KWY9J35")).thenReturn(true);
        CarController controller = new CarController(carsService);

        var response = controller.create(carRequest());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getHttpStatus());
        assertFalse(response.getBody().isSuccess());
        verify(carsService, never()).create(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void updateReturnsConflictWhenPlateBelongsToAnotherCar() {
        Cars existingCar = new Cars();
        existingCar.setId(2L);
        when(carsService.existsById(1L)).thenReturn(true);
        when(carsService.findByPlates("KWY9J35")).thenReturn(Optional.of(existingCar));
        CarController controller = new CarController(carsService);

        var response = controller.update(1L, carRequest());

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().getHttpStatus());
        assertFalse(response.getBody().isSuccess());
        verify(carsService, never()).update(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void createReturnsNotFoundWhenCustomerDoesNotExist() {
        CarReq request = carRequest();
        request.setCustomerId(99L);
        when(carsService.existsByPlates("KWY9J35")).thenReturn(false);
        when(carsService.customerExists(99L)).thenReturn(false);
        CarController controller = new CarController(carsService);

        var response = controller.create(request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("CUSTOMER_NOT_FOUND", response.getBody().getError().getCode());
        verify(carsService, never()).create(org.mockito.ArgumentMatchers.any());
    }

    private CarReq carRequest() {
        CarReq request = new CarReq();
        request.setBrand("Volkswagen");
        request.setModel("Gol GTI");
        request.setColor("Azul");
        request.setYearManufacture(1994);
        request.setImported(false);
        request.setPlates("KWY9J35");
        return request;
    }
}
