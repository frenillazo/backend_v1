package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Grupo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface GrupoRepository extends JpaRepository<Grupo, Long> {

    /* estado temporal --------------------------------------------------- */
    List<Grupo> findByFechaInicioBeforeAndFechaFinAfter(LocalDate hoy);               // activos
    List<Grupo> findByFechaFinBefore(LocalDate hoy);                                  // finalizados
    List<Grupo> findByFechaInicioAfter(LocalDate hoy);                                // futuros
    List<Grupo> findByFechaInicioBetween(LocalDate inicio, LocalDate fin);

    /* analítica / agregados --------------------------------------------- */
    @Query("""
           select g, count(i)
           from Grupo g
                left join g.inscripciones i
           group by g
           """)
    List<Object[]> findAllWithNumAlumnos();   // g, nº alumnos

    /* navegación -------------------------------------------------------- */
    @Query("""
           select g
           from Grupo g
                join g.inscripciones i
           where i.alumno.id = :alumnoId
           """)
    List<Grupo> findByAlumnoId(Long alumnoId);

     /* ➕ Grupos con AL MENOS X alumnos */
    @Query("""
           select g
           from Grupo g
                left join g.inscripciones i
           group by g
           having count(i) >= :minAlumnos
           """)
    List<Grupo> findConMinAlumnos(long minAlumnos);

    /* ➖ Grupos con MENOS de X alumnos */
    @Query("""
           select g
           from Grupo g
                left join g.inscripciones i
           group by g
           having count(i) < :maxAlumnos
           """)
    List<Grupo> findConMaxAlumnos(long maxAlumnos);

    /* 0️⃣ Grupos vacíos (sin alumnos) */
    @Query("""
           select g
           from Grupo g
           where g.inscripciones is empty
           """)
    List<Grupo> findSinAlumnos();

    /* 🏅 Top-N grupos con más alumnos */
    @Query("""
           select g
           from Grupo g
                left join g.inscripciones i
           group by g
           order by count(i) desc
           """)
    Page<Grupo> findTopMasAlumnos(Pageable topN);

    /* ⏳ Grupos que arrancan en los próximos X días */
    @Query("""
           select g
           from Grupo g
           where g.fechaInicio between current_date and :limite
           order by g.fechaInicio
           """)
    List<Grupo> findQueEmpiezanAntesDe(LocalDate limite);

    /* 📅 Grupos activos HOY (ya los sugerimos, pero completamos con proyección) */
    @Query("""
           select g.id as id,
                  g.fechaInicio as inicio,
                  g.fechaFin as fin,
                  count(i) as numAlumnos
           from Grupo g
                left join g.inscripciones i
           where :hoy between g.fechaInicio and g.fechaFin
           group by g
           """)
    List<GrupoResumen> findActivosConNumAlumnos(LocalDate hoy);

    interface GrupoResumen {
        Long getId();
        LocalDate getInicio();
        LocalDate getFin();
        long getNumAlumnos();
    }
}
