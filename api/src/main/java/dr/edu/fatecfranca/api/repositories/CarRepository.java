package dr.edu.fatecfranca.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import dr.edu.fatecfranca.api.entities.Car;

public interface CarRepository extends JpaRepository<Car, Long> {

    boolean existsByPlates(String plates);

    boolean existsByPlatesAndIdNot(String plates, Long id);
}
