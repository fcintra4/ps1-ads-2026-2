package br.edu.fatecfranca.api.services;

import br.edu.fatecfranca.api.dtos.UserRequest;
import br.edu.fatecfranca.api.entities.User;
import br.edu.fatecfranca.api.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User create(UserRequest request) {
        return repository.save(toEntity(request));
    }

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User update(Long id, UserRequest request) {
        User user = toEntity(request);
        user.setId(id);
        return repository.save(user);
    }

    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    private User toEntity(UserRequest request) {
        User user = new User();
        user.setFullname(request.fullname());
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(request.password());
        user.setIsAdmin(request.isAdmin() == null ? false : request.isAdmin());
        return user;
    }
}