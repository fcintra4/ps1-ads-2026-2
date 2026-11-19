package br.edu.fatecfranca.api.repositories;

import br.edu.fatecfranca.api.entities.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByPlates(String plates);
    boolean existsByPlatesAndIdNot(String plates, Long id);

}
