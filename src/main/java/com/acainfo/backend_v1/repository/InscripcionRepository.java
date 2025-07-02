package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    /* ---------- Accesos directos ---------- */
    Optional<Inscripcion> findByAlumnoIdAndGrupoId(Long alumnoId, Long grupoId);
    boolean existsByAlumnoIdAndGrupoId(Long alumnoId, Long grupoId);

    List<Inscripcion> findByAlumnoId(Long alumnoId);
    List<Inscripcion> findByGrupoId(Long grupoId);
    long countByGrupoId(Long grupoId);

    /* ---------- Rango temporal ---------- */
    List<Inscripcion> findByFechaInscripcionBetween(LocalDate desde, LocalDate hasta);

    /* ---------- Consultas de conveniencia ---------- */

    /** IDs de alumnos inscritos a un grupo (útil para no traer entidades completas) */
    @Query("""
           select i.alumno.id
           from Inscripcion i
           where i.grupo.id = :grupoId
           """)
    List<Long> findIdsAlumnosEnGrupo(@Param("grupoId") Long grupoId);
}


