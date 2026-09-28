package br.bea.fatecfranca.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.bea.fatecfranca.api.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {

}