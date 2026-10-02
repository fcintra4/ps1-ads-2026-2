package br.edu.fatecfranca.api.controllers;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.patterns.decorator.SalePrice;
import br.edu.fatecfranca.api.services.CarQuoteService;
import br.edu.fatecfranca.api.services.CarSaleService;
import br.edu.fatecfranca.api.services.CarValuationService;

@RestController
@RequestMapping("/cars/{carId}")
public class CarPatternController {

  private final CarSaleService saleService;
  private final CarValuationService valuationService;
  private final CarQuoteService quoteService;

  public CarPatternController(
          CarSaleService saleService,
          CarValuationService valuationService,
          CarQuoteService quoteService) {
    this.saleService = saleService;
    this.valuationService = valuationService;
    this.quoteService = quoteService;
  }

  @GetMapping("/sale/state")
  public Map<String, String> currentState(@PathVariable Long carId) {
    return Map.of("state", saleService.currentState(carId));
  }

  @PostMapping("/sale/reserve")
  public ResponseEntity<Car> reserve(
          @PathVariable Long carId,
          @RequestBody ReserveRequest request) {
    return ResponseEntity.ok(saleService.reserve(carId, request.customerId()));
  }

  @PostMapping("/sale/complete")
  public ResponseEntity<Car> complete(
          @PathVariable Long carId,
          @RequestBody CompleteSaleRequest request) {
    return ResponseEntity.ok(saleService.complete(carId, request.sellingPrice()));
  }

  @PostMapping("/sale/cancel")
  public ResponseEntity<Car> cancel(@PathVariable Long carId) {
    return ResponseEntity.ok(saleService.cancelReservation(carId));
  }

  @GetMapping("/valuation")
  public ValuationResponse valuation(@PathVariable Long carId) {
    return new ValuationResponse(carId, valuationService.estimate(carId));
  }

  @PostMapping("/quote")
  public QuoteResponse quote(
          @PathVariable Long carId,
          @RequestBody QuoteRequest request) {
    SalePrice price = quoteService.quote(
            carId,
            request.basePrice(),
            request.includeDocumentation(),
            request.loyaltyDiscountPercent());

    return new QuoteResponse(carId, price.total(), price.description());
  }

  public record ReserveRequest(Long customerId) {
  }

  public record CompleteSaleRequest(BigDecimal sellingPrice) {
  }

  public record QuoteRequest(
          BigDecimal basePrice,
          boolean includeDocumentation,
          BigDecimal loyaltyDiscountPercent) {
  }

  public record QuoteResponse(Long carId, BigDecimal total, String composition) {
  }

  public record ValuationResponse(Long carId, BigDecimal estimatedValue) {
  }
}
