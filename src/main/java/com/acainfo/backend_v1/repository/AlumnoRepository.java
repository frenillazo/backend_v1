package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    /* ---------- Búsquedas por campos únicos ---------- */
    Optional<Alumno> findByEmail(String email);
    Optional<Alumno> findByTelefono(String telefono);

    /* ---------- Filtros comunes ---------- */
    List<Alumno> findByCarrera(String carrera);
    List<Alumno> findByCarreraAndCurso(String carrera, String curso);
    List<Alumno> findByNombreContainingIgnoreCase(String nombre);

    /* ---------- Comprobaciones de existencia ---------- */
    boolean existsByEmail(String email);
    boolean existsByTelefono(String telefono);

    /* ---------- Consultas compuestas ---------- */

    /** Alumnos inscritos en un grupo concreto */
    @Query("""
           select i.alumno
           from Inscripcion i
           where i.grupo.id = :grupoId
           """)
    List<Alumno> findAllByGrupoId(@Param("grupoId") Long grupoId);

    /** Alumnos que aún no pertenecen a ningún grupo */
    @Query("""
           select a
           from Alumno a
           where a.inscripciones is empty
           """)
    List<Alumno> findAlumnosSinGrupos();
}