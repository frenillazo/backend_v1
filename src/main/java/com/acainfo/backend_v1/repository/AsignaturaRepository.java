package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Asignatura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Long> {

    /* ---------- Búsquedas principales ---------- */
    Optional<Asignatura> findByNombreAndCarrera(String nombre, String carrera);
    List<Asignatura> findByCarreraOrderByNombreAsc(String carrera);

    /* ---------- Comprobaciones y métricas ---------- */
    boolean existsByNombreAndCarrera(String nombre, String carrera);
    long countByCarrera(String carrera);

    /* ---------- Consultas especializadas ---------- */

    /** Carreras distintas disponibles en el sistema */
    @Query("select distinct a.carrera from Asignatura a order by a.carrera")
    List<String> findCarrerasDisponibles();

    /** Asignaturas que aún no tienen material asociado */
    @Query("select a from Asignatura a where a.materiales is empty")
    List<Asignatura> findAsignaturasSinMaterial();
}

