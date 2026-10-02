package br.edu.fatecfranca.api.patterns.state;

public class InvalidSaleStateException extends RuntimeException {

  public InvalidSaleStateException(String message) {
    super(message);
  }
}
