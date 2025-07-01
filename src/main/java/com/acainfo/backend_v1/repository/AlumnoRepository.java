package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Alumno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;



public interface AlumnoRepository extends JpaRepository<Alumno, Long> {

    /* clave natural ----------------------------------------------------- */
    Optional<Alumno> findByEmail(String email);          // único → lookup rápido
    boolean existsByEmail(String email);                 // para validaciones

    /* filtros frecuentes ------------------------------------------------ */
    List<Alumno> findByCarrera(String carrera);
    List<Alumno> findByCarreraAndCurso(String carrera, String curso);
    List<Alumno> findByNombreContainingIgnoreCase(String nombre);

    /* relaciones -------------------------------------------------------- */
    @Query("""
           select a
           from Alumno a
                join a.inscripciones i
           where i.grupo.id = :grupoId
           """)
    List<Alumno> findByGrupoId(Long grupoId);

    long countByInscripciones_Grupo_Id(Long grupoId);    // nº de alumnos en un grupo

    @Query("""
           select a
           from Alumno a
                join a.inscripciones i
           where i.grupo.id = :grupoId
           order by a.nombre
           """)
    List<Alumno> findByGrupoIdOrderByNombre(Long grupoId);

    @Query("""
           select a
           from Alumno a
                join a.inscripciones i
           where i.grupo.id = :grupoId
           """)
    Page<Alumno> findByGrupoId(Long grupoId, Pageable pageable);

    @Query("""
           select a
           from Alumno a
                join a.inscripciones i
           where i.grupo.id = :grupoId
             and i.fechaInscripcion >= :desde
           """)
    List<Alumno> findByGrupoIdAndFechaInscripcionAfter(Long grupoId, LocalDate desde);

    @Query("select a from Alumno a where a.inscripciones is empty")
    List<Alumno> findSinGrupo();

    @Query("""
           select a
           from Alumno a
           where size(a.inscripciones) > :minGrupos
           """)
    List<Alumno> findConMasDeNGrupos(int minGrupos);
}
