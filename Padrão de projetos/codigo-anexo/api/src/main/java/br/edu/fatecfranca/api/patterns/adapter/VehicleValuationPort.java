package br.edu.fatecfranca.api.patterns.adapter;

import java.math.BigDecimal;

import br.edu.fatecfranca.api.entities.Car;

public interface VehicleValuationPort {

  BigDecimal estimate(Car car);
}
