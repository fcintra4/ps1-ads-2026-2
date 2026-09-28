package br.edu.fatecfranca.api.logic.domain;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.edu.fatecfranca.api.entities.Users;
import br.edu.fatecfranca.api.repositories.interfaces.UserRepository;

public class UsersDomain {

    private final UserRepository repository;

    public UsersDomain(UserRepository repository) { this.repository = repository; }

    public Users create(Users user) { return repository.save(user); }
    public List<Users> findAll() { return repository.findAll(); }
    public Page<Users> findAll(Pageable pageable) { return repository.findAll(pageable); }
    public Optional<Users> findById(Long id) { return repository.findById(id); }
    public Users update(Users user) { return repository.save(user); }
    public boolean existsById(Long id) { return repository.existsById(id); }
    public void deleteById(Long id) { repository.deleteById(id); }

    public Optional<Users> findByUsername(String username) { return repository.findByUsername(username); }
    public boolean existsByUsername(String username) { return repository.existsByUsername(username); }
    public Optional<Users> findByEmail(String email) { return repository.findByEmail(email); }
    public boolean existsByEmail(String email) { return repository.existsByEmail(email); }
}