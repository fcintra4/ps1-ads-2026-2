package br.edu.fatecfranca.api.patterns;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import br.edu.fatecfranca.api.patterns.decorator.BaseSalePrice;
import br.edu.fatecfranca.api.patterns.decorator.DocumentationFeeDecorator;
import br.edu.fatecfranca.api.patterns.decorator.ImportedVehicleFeeDecorator;
import br.edu.fatecfranca.api.patterns.decorator.LoyaltyDiscountDecorator;
import br.edu.fatecfranca.api.patterns.decorator.SalePrice;

class SalePriceDecoratorTests {

  @Test
  void composesOptionalFeesAndDiscount() {
    SalePrice price = new BaseSalePrice(new BigDecimal("100000.00"));
    price = new ImportedVehicleFeeDecorator(price);
    price = new DocumentationFeeDecorator(price);
    price = new LoyaltyDiscountDecorator(price, new BigDecimal("10"));

    assertEquals(new BigDecimal("93465.00"), price.total());
    assertEquals(
            "base price + 3% imported vehicle fee + documentation fee"
                    + " - 10% loyalty discount",
            price.description());
  }
}
