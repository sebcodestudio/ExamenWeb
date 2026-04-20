package pe.com.hermes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.com.hermes.model.Persona;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByCorreo(String correo);

    @Query("SELECT p FROM Persona p WHERE " +
            "LOWER(p.nombres) LIKE LOWER(CONCAT('%', :filtro, '%')) OR " +
            "LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :filtro, '%'))")
    List<Persona> buscarPorNombreOApellido(@Param("filtro") String filtro);
}