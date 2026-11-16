package br.edu.fatecfranca.api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecfranca.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);
}