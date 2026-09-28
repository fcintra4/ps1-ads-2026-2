package br.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.Users;
import br.edu.fatecfranca.api.logic.domain.UsersDomain;
import br.edu.fatecfranca.api.repositories.interfaces.UserRepository;

@Service
public class UsersService {

    private final UsersDomain domain;

    public UsersService(UserRepository repository) {
        this.domain = new UsersDomain(repository);
    }

    public Users create(Users user) { return domain.create(user); }
    public List<Users> findAll() { return domain.findAll(); }
    public Page<Users> findAll(Pageable pageable) { return domain.findAll(pageable); }
    public Optional<Users> findById(Long id) { return domain.findById(id); }
    public Users update(Users user) { return domain.update(user); }
    public boolean existsById(Long id) { return domain.existsById(id); }
    public void deleteById(Long id) { domain.deleteById(id); }

    public Optional<Users> findByUsername(String username) { return domain.findByUsername(username); }
    public boolean existsByUsername(String username) { return domain.existsByUsername(username); }
    public Optional<Users> findByEmail(String email) { return domain.findByEmail(email); }
    public boolean existsByEmail(String email) { return domain.existsByEmail(email); }
}