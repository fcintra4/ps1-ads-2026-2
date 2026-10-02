package br.edu.fatecfranca.api.patterns.adapter;

import java.time.Year;

import org.springframework.stereotype.Component;

@Component
public class LegacyValuationClient {

  public long calculateValueInCents(
          String vehicleName,
          int fabricationYear,
          String originCode) {
    int age = Math.max(0, Year.now().getValue() - fabricationYear);
    long valueInCents = Math.max(1_500_000L, 9_000_000L - age * 450_000L);

    if ("IMP".equals(originCode)) {
      valueInCents = Math.round(valueInCents * 1.12);
    }

    return valueInCents;
  }
}
