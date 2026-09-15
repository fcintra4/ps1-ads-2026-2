package br.edu.fatecfranca.api.repositories.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.edu.fatecfranca.api.entities.Cars;

public interface CarRepository extends JpaRepository<Cars, Long> {

    // Índice: uq_cars_plates
    @Query("SELECT c FROM Cars c WHERE c.plates = :plates")
    Optional<Cars> findByPlates(@Param("plates") String plates);

    @Query("SELECT COUNT(c) > 0 FROM Cars c WHERE c.plates = :plates")
    boolean existsByPlates(@Param("plates") String plates);

    // Índice: idx_cars_customer_id (Navega até o ID do objeto Customer associado)
    @Query("SELECT c FROM Cars c WHERE c.customer.id = :customerId")
    List<Cars> findByCustomerId(@Param("customerId") Long customerId);

    // Índice Composto: idx_cars_brand_model
    @Query("SELECT c FROM Cars c WHERE c.brand = :brand AND c.model = :model")
    List<Cars> findByBrandAndModel(@Param("brand") String brand, @Param("model") String model);

    @Query("SELECT c FROM Cars c WHERE c.brand = :brand")
    List<Cars> findByBrand(@Param("brand") String brand);

    // Índice: idx_cars_selling_date
    @Query("SELECT c FROM Cars c WHERE c.sellingDate = :sellingDate")
    List<Cars> findBySellingDate(@Param("sellingDate") LocalDate sellingDate);

    @Query("SELECT c FROM Cars c WHERE c.sellingDate BETWEEN :startDate AND :endDate")
    List<Cars> findBySellingDateBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}