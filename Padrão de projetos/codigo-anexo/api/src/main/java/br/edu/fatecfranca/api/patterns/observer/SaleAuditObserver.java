package br.edu.fatecfranca.api.patterns.observer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SaleAuditObserver implements CarSaleObserver {

  private static final Logger LOGGER = LoggerFactory.getLogger(SaleAuditObserver.class);

  @Override
  public void onCarSold(CarSoldEvent event) {
    LOGGER.info(
            "Sale audit: car={}, plates={}, customer={}, price={}, date={}",
            event.carId(),
            event.plates(),
            event.customerId(),
            event.sellingPrice(),
            event.sellingDate());
  }
}
