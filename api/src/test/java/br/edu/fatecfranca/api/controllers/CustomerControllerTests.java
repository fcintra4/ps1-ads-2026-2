package br.edu.fatecfranca.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.fatecfranca.api.entities.Customer;
import br.edu.fatecfranca.api.repositories.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTests {

  @Mock
  private CustomerRepository repository;

  @InjectMocks
  private CustomerController controller;

  @Test
  void findByIdReturnsCustomerWhenItExists() {
    Customer customer = new Customer();
    customer.setId(4L);
    when(repository.findById(4L)).thenReturn(Optional.of(customer));

    ResponseEntity<Customer> response = controller.findById(4L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertSame(customer, response.getBody());
  }

  @Test
  void findByIdReturnsNotFoundWhenCustomerDoesNotExist() {
    when(repository.findById(19L)).thenReturn(Optional.empty());

    ResponseEntity<Customer> response = controller.findById(19L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateReturnsSavedCustomerWhenItExists() {
    Customer customer = new Customer();
    when(repository.existsById(5L)).thenReturn(true);
    when(repository.save(customer)).thenReturn(customer);

    ResponseEntity<Customer> response = controller.update(5L, customer);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(5L, customer.getId());
    assertSame(customer, response.getBody());
    verify(repository).save(customer);
  }

  @Test
  void updateReturnsNotFoundWhenCustomerDoesNotExist() {
    Customer customer = new Customer();
    when(repository.existsById(25L)).thenReturn(false);

    ResponseEntity<Customer> response = controller.update(25L, customer);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(repository, never()).save(customer);
  }

  @Test
  void deleteReturnsNoContentWhenCustomerExists() {
    when(repository.existsById(3L)).thenReturn(true);

    ResponseEntity<Void> response = controller.delete(3L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(repository).deleteById(3L);
  }

  @Test
  void deleteReturnsNotFoundWhenCustomerDoesNotExist() {
    when(repository.existsById(11L)).thenReturn(false);

    ResponseEntity<Void> response = controller.delete(11L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(repository, never()).deleteById(11L);
  }
}
