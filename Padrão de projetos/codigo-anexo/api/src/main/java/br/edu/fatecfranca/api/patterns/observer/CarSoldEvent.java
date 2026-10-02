package br.edu.fatecfranca.api.patterns.observer;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CarSoldEvent(
        Long carId,
        String plates,
        Long customerId,
        String customerEmail,
        BigDecimal sellingPrice,
        LocalDate sellingDate) {
}
