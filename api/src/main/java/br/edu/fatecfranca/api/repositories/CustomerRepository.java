package br.edu.fatecfranca.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecfranca.api.entities.Customers;

public interface CustomerRepository extends JpaRepository<Customers, Long> {
}
