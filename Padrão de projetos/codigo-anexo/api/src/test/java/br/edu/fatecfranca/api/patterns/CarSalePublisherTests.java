package br.edu.fatecfranca.api.patterns;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import br.edu.fatecfranca.api.patterns.observer.CarSaleObserver;
import br.edu.fatecfranca.api.patterns.observer.CarSalePublisher;
import br.edu.fatecfranca.api.patterns.observer.CarSoldEvent;

class CarSalePublisherTests {

  @Test
  void notifiesEveryRegisteredObserver() {
    CarSaleObserver first = mock(CarSaleObserver.class);
    CarSaleObserver second = mock(CarSaleObserver.class);
    CarSalePublisher publisher = new CarSalePublisher(List.of(first, second));
    CarSoldEvent event = new CarSoldEvent(
            1L,
            "ABC0B23",
            2L,
            "cliente@example.com",
            new BigDecimal("80000.00"),
            LocalDate.of(2026, 10, 2));

    publisher.publish(event);

    verify(first).onCarSold(event);
    verify(second).onCarSold(event);
  }
}
