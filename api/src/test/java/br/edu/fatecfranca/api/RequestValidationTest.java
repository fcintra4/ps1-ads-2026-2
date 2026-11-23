package br.edu.fatecfranca.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CarReq;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.CustomerReq;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.UserReq;
import br.edu.fatecfranca.api.services.policy.common.http.pkg.pipes.reqs.ValidationGroups;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class RequestValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void patchAllowsOmittedFieldsButValidatesProvidedValues() {
        CarReq request = new CarReq();
        request.setBrand("Honda");

        assertTrue(validator.validate(request).isEmpty());

        request.setYearManufacture(2101);
        assertFalse(validator.validate(request).isEmpty());
    }

    @Test
    void fullUserRequestRequiresFieldsAndValidatesPasswordLength() {
        UserReq request = new UserReq();
        request.setFullname("Ana Souza");
        request.setUsername("ana.souza");
        request.setEmail("ana@example.com");
        request.setPassword("short");

        Set<?> violations = validator.validate(request, ValidationGroups.Required.class);

        assertTrue(violations.stream().anyMatch(violation ->
            violation.toString().contains("password")
        ));
        assertFalse(validator.validate(new UserReq(), ValidationGroups.Required.class).isEmpty());
    }

    @Test
    void userRejectsMalformedNameUsernameAndEmail() {
        UserReq request = new UserReq();
        request.setFullname("Ana123");
        request.setUsername("ab");
        request.setEmail("invalid-email");
        request.setPassword("ValidPass123");

        assertFalse(validator.validate(request, ValidationGroups.Required.class).isEmpty());
    }

    @Test
    void customerAcceptsValidCpfAndBrazilianPhone() {
        CustomerReq request = validCustomer();

        assertTrue(validator.validate(request, ValidationGroups.Required.class).isEmpty());
    }

    @Test
    void customerAcceptsValidCnpjAndNormalizesIt() {
        CustomerReq request = validCustomer();
        request.setIdentDocument("11.222.333/0001-81");

        assertTrue(validator.validate(request, ValidationGroups.Required.class).isEmpty());
        assertEquals("11222333000181", request.toEntity().getIdentDocument());
    }

    @Test
    void customerRejectsInvalidCpfAndPhone() {
        CustomerReq request = validCustomer();
        request.setIdentDocument("111.111.111-11");
        request.setPhone("(00) 1234-5678");

        assertFalse(validator.validate(request, ValidationGroups.Required.class).isEmpty());
    }

    @Test
    void carRejectsMalformedBrazilianPlateAndExcessivePricePrecision() {
        CarReq request = new CarReq();
        request.setBrand("Volkswagen");
        request.setModel("Gol GTI");
        request.setColor("Azul");
        request.setYearManufacture(1994);
        request.setImported(false);
        request.setPlates("ABC----");
        request.setSellingPrice(new java.math.BigDecimal("12345678901.123"));

        assertFalse(validator.validate(request, ValidationGroups.Required.class).isEmpty());
    }

    private CustomerReq validCustomer() {
        CustomerReq request = new CustomerReq();
        request.setName("Ana Souza");
        request.setIdentDocument("529.982.247-25");
        request.setStreetName("Rua das Flores");
        request.setHouseNumber("123");
        request.setDistrict("Centro");
        request.setMunicipality("Franca");
        request.setState("sp");
        request.setPhone("(11) 98765-4321");
        request.setEmail("ana@example.com");
        return request;
    }
}