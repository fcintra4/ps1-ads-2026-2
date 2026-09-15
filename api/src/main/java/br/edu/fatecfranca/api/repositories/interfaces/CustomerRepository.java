package br.edu.fatecfranca.api.repositories.interfaces;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import br.edu.fatecfranca.api.entities.Customers;

public interface CustomerRepository extends JpaRepository<Customers, Long> {

    // Índice: idx_customers_ident_document
    @Query("SELECT c FROM Customers c WHERE c.identDocument = :identDocument")
    Optional<Customers> findByIdentDocument(@Param("identDocument") String identDocument);

    @Query("SELECT COUNT(c) > 0 FROM Customers c WHERE c.identDocument = :identDocument")
    boolean existsByIdentDocument(@Param("identDocument") String identDocument);

    // Índice: idx_customers_email
    @Query("SELECT c FROM Customers c WHERE c.email = :email")
    Optional<Customers> findByEmail(@Param("email") String email);

    @Query("SELECT COUNT(c) > 0 FROM Customers c WHERE c.email = :email")
    boolean existsByEmail(@Param("email") String email);

    // Índice: idx_customers_name
    @Query("SELECT c FROM Customers c WHERE c.name = :name")
    List<Customers> findByName(@Param("name") String name);

    @Query("SELECT c FROM Customers c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Customers> findByNameContainingIgnoreCase(@Param("name") String name);

    // Índice Composto: idx_customers_state_municipality
    @Query("SELECT c FROM Customers c WHERE c.state = :state AND c.municipality = :municipality")
    List<Customers> findByStateAndMunicipality(@Param("state") String state, @Param("municipality") String municipality);

    @Query("SELECT c FROM Customers c WHERE c.state = :state")
    List<Customers> findByState(@Param("state") String state);
}