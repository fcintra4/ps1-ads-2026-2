package br.edu.fatecfranca.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.edu.fatecfranca.api.entities.User;
import br.edu.fatecfranca.api.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserControllerTests {

  @Mock
  private UserService service;

  @InjectMocks
  private UserController controller;

  @Test
  void createReturnsCreatedUser() {
    User user = new User();
    when(service.create(user)).thenReturn(user);

    ResponseEntity<User> response = controller.create(user);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertSame(user, response.getBody());
  }

  @Test
  void findAllReturnsUsers() {
    List<User> users = List.of(new User(), new User());
    when(service.findAll()).thenReturn(users);

    List<User> response = controller.findAll();

    assertSame(users, response);
  }

  @Test
  void findByIdReturnsUserWhenItExists() {
    User user = new User();
    user.setId(1L);
    when(service.findById(1L)).thenReturn(Optional.of(user));

    ResponseEntity<User> response = controller.findById(1L);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertSame(user, response.getBody());
  }

  @Test
  void findByIdReturnsNotFoundWhenUserDoesNotExist() {
    when(service.findById(3L)).thenReturn(Optional.empty());

    ResponseEntity<User> response = controller.findById(3L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  @Test
  void updateReturnsSavedUserWhenItExists() {
    User user = new User();
    when(service.existsById(1L)).thenReturn(true);
    when(service.update(user)).thenReturn(user);

    ResponseEntity<User> response = controller.update(1L, user);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1L, user.getId());
    assertSame(user, response.getBody());
    verify(service).update(user);
  }

  @Test
  void updateReturnsNotFoundWhenUserDoesNotExist() {
    User user = new User();
    when(service.existsById(3L)).thenReturn(false);

    ResponseEntity<User> response = controller.update(3L, user);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(service, never()).update(user);
  }

  @Test
  void deleteReturnsNoContentWhenUserExists() {
    when(service.existsById(3L)).thenReturn(true);

    ResponseEntity<Void> response = controller.delete(3L);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    verify(service).deleteById(3L);
  }

  @Test
  void deleteReturnsNotFoundWhenUserDoesNotExist() {
    when(service.existsById(3L)).thenReturn(false);

    ResponseEntity<Void> response = controller.delete(3L);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    verify(service, never()).deleteById(3L);
  }
}
