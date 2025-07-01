package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    /* clave natural (UNIQUE nombre+carrera) ----------------------------- */
    Optional<Asignatura> findByNombreAndCarrera(String nombre, String carrera);
    boolean existsByNombreAndCarrera(String nombre, String carrera);

    /* catálogos --------------------------------------------------------- */
    List<Asignatura> findByCarrera(String carrera);
    List<Asignatura> findByNombreContainingIgnoreCase(String nombre);
}
