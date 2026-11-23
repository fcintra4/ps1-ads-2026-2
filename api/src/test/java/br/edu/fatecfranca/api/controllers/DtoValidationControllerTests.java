package br.edu.fatecfranca.api.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.entities.User;
import br.edu.fatecfranca.api.services.CarService;
import br.edu.fatecfranca.api.services.TokenService;
import br.edu.fatecfranca.api.services.UserService;

class DtoValidationControllerTests {
    private static final String CAR = """
            {"brand":"Honda","model":"Civic","color":"Preto",
             "yearManufacture":2021,"imported":false,"plates":"ABC1D23",
             "sellingDate":null,"sellingPrice":null,"customerId":null}
            """;
    private static final String USER = """
            {"fullname":"Marina Ribeiro","username":"Marina.ribeiro_1-teste",
             "email":"marina@example.com","password":"senha1234","isAdmin":false}
            """;

    private final CarService cars = mock(CarService.class);
    private final UserService users = mock(UserService.class);
    private final TokenService tokens = mock(TokenService.class);
    private final MockMvc mvc = MockMvcBuilders.standaloneSetup(
            new CarController(cars), new UserController(users, tokens)).build();

    static Stream<String> invalidCars() {
        return Stream.of(
                CAR.replace("\"Honda\"", "\" \""),
                CAR.replace("\"Civic\"", "null"),
                CAR.replace("\"Preto\"", "\"\""),
                CAR.replace("2021", "null"),
                CAR.replace("2021", "0"),
                CAR.replace("false", "null"),
                CAR.replace("\"ABC1D23\"", "null"),
                CAR.replace("\"ABC1D23\"", "\"\""),
                CAR.replace("\"ABC1D23\"", "\"abc1d23\""),
                CAR.replace("\"ABC1D23\"", "\"ABC1234\""),
                CAR.replace("\"sellingDate\":null", "\"sellingDate\":\"" + LocalDate.now().plusDays(1) + "\""),
                CAR.replace("\"sellingPrice\":null", "\"sellingPrice\":10000000000"),
                CAR.replace("\"sellingPrice\":null", "\"sellingPrice\":1.001"),
                CAR.replace("\"customerId\":null", "\"customerId\":0"),
                CAR.replace("\"customerId\":null", "\"customerId\":-1"));
    }

    @ParameterizedTest
    @MethodSource("invalidCars")
    void invalidCarIsRejectedOnPostAndPutBeforeService(String payload) throws Exception {
        mvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/cars/7").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(cars);
    }

    static Stream<String> invalidUsers() {
        return Stream.of(
                USER.replace("\"Marina Ribeiro\"", "\" \""),
                USER.replace("\"Marina.ribeiro_1-teste\"", "null"),
                USER.replace("\"Marina.ribeiro_1-teste\"", "\"marína\""),
                USER.replace("\"Marina.ribeiro_1-teste\"", "\"marina ribeiro\""),
                USER.replace("\"Marina.ribeiro_1-teste\"", "\"marina@\""),
                USER.replace("\"marina@example.com\"", "\"\""),
                USER.replace("\"marina@example.com\"", "\"invalido\""),
                USER.replace("\"senha1234\"", "null"),
                USER.replace("\"senha1234\"", "\"        \""),
                USER.replace("\"senha1234\"", "\"1234567\""),
                USER.replace("false", "null"));
    }

    @ParameterizedTest
    @MethodSource("invalidUsers")
    void invalidUserIsRejectedOnPostAndPutBeforeService(String payload) throws Exception {
        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest());
        mvc.perform(put("/users/7").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(users);
    }

    static Stream<String> validCars() {
        return Stream.of(CAR, CAR.replace("ABC1D23", "ABC-1234"),
                CAR.replace("\"sellingDate\":null", "\"sellingDate\":\"" + LocalDate.now() + "\"")
                        .replace("\"sellingPrice\":null", "\"sellingPrice\":9999999999.99")
                        .replace("\"customerId\":null", "\"customerId\":1"));
    }

    @ParameterizedTest
    @MethodSource("validCars")
    void validCarReachesServiceOnPostAndPut(String payload) throws Exception {
        when(cars.create(any())).thenReturn(new Car());
        when(cars.existsById(7L)).thenReturn(true);
        when(cars.update(eq(7L), any())).thenReturn(new Car());

        mvc.perform(post("/cars").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk());
        mvc.perform(put("/cars/7").contentType(MediaType.APPLICATION_JSON).content(payload))
                .andExpect(status().isOk());
    }

    @Test
    void validUserReachesServiceAndResponseOmitsPassword() throws Exception {
        User user = new User();
        user.setPassword("hash-que-nao-deve-ser-exposto");
        when(users.create(any())).thenReturn(user);
        when(users.existsById(7L)).thenReturn(true);
        when(users.update(eq(7L), any())).thenReturn(user);

        mvc.perform(post("/users").contentType(MediaType.APPLICATION_JSON).content(USER))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.password").doesNotExist());
        mvc.perform(put("/users/7").contentType(MediaType.APPLICATION_JSON).content(USER))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.password").doesNotExist());
    }
}
