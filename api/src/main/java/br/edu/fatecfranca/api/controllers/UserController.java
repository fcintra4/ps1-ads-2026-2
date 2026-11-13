package br.edu.fatecfranca.api.controllers;

import java.util.List;

import br.edu.fatecfranca.api.dtos.UserResponse;

import jakarta.validation.Valid;

import br.edu.fatecfranca.api.dtos.LoginRequest;
import br.edu.fatecfranca.api.dtos.UserResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.dtos.UserRequest;
import br.edu.fatecfranca.api.entities.User;
import br.edu.fatecfranca.api.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
           @Valid @RequestBody LoginRequest request) {


       return service.authenticate(request)
               .map(UserResponse::new)
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity
                       .status(HttpStatus.UNAUTHORIZED)
                       .build());
   }


    @GetMapping
   public List<UserResponse> findAll() {
       return service.findAll()
               .stream()
               .map(UserResponse::new)
               .toList();
   }

    @GetMapping("/{id}")
   public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
       return service.findById(id)
               .map(UserResponse::new)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
   }


    @PutMapping("/{id}")
   public ResponseEntity<UserResponse> update(
           @PathVariable Long id,
           @RequestBody UserRequest request) {


       if (!service.existsById(id)) {
           return ResponseEntity.notFound().build();
       }

       User updatedUser = service.update(id, request);

       return ResponseEntity.ok(new UserResponse(updatedUser));
   }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
