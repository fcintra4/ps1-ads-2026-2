package br.edu.fatecfranca.api.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecfranca.api.entities.Cars;

public interface CarRepository extends JpaRepository<Cars, Long> {}
