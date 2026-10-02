package br.edu.fatecfranca.api.patterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class CustomerNotificationObserver implements CarSaleObserver {

  private static final Logger LOGGER =
          LoggerFactory.getLogger(CustomerNotificationObserver.class);

  @Override
  public void onCarSold(CarSoldEvent event) {
    LOGGER.info(
            "Sale confirmation queued for {} concerning vehicle {}.",
            event.customerEmail(),
            event.plates());
  }
}
