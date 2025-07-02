package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /* ---------- Estado temporal ---------- */

    /** Grupos activos en una fecha concreta (fecha ∈ [inicio, fin]) */
    @Query("""
           select g
           from Grupo g
           where :fecha between g.fechaInicio and g.fechaFin
           """)
    List<Grupo> findGruposActivosEnFecha(@Param("fecha") LocalDate fecha);

    /* ---------- Por asignatura ---------- */
    List<Grupo> findByAsignaturaId(Long asignaturaId);

    @Query("""
           select g
           from Grupo g
           where g.asignatura.nombre = :nombre
             and g.asignatura.carrera = :carrera
           """)
    List<Grupo> findByAsignaturaNombreAndCarrera(@Param("nombre") String nombre,
                                                 @Param("carrera") String carrera);

    /* ---------- Relación con alumnos ---------- */

    /** Grupos en los que está inscrito un alumno */
    @Query("""
           select i.grupo
           from Inscripcion i
           where i.alumno.id = :alumnoId
           """)
    List<Grupo> findGruposByAlumnoId(@Param("alumnoId") Long alumnoId);

    /** Número de alumnos en un grupo */
    @Query("""
           select count(i)
           from Inscripcion i
           where i.grupo.id = :grupoId
           """)
    long countAlumnos(@Param("grupoId") Long grupoId);

    /** Grupos sin ningún alumno inscrito */
    @Query("select g from Grupo g where g.inscripciones is empty")
    List<Grupo> findGruposVacios();
}

