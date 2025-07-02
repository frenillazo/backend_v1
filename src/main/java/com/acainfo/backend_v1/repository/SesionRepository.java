package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

    /* ---------- Filtros directos ---------- */
    List<Sesion> findByGrupoId(Long grupoId);
    List<Sesion> findByDiaSemanaIgnoreCase(String diaSemana);
    List<Sesion> findByGrupoIdAndDiaSemanaIgnoreCase(Long grupoId, String diaSemana);

    /* ---------- Rangos horarios ---------- */
    List<Sesion> findByGrupoIdAndHoraInicioBetween(Long grupoId,
                                                   LocalTime desde,
                                                   LocalTime hasta);

    /* ---------- Ordenación convenida ---------- */
    List<Sesion> findByGrupoIdOrderByDiaSemanaAscHoraInicioAsc(Long grupoId);

    /* ---------- Métricas ---------- */
    long countByGrupoId(Long grupoId);

    /** Duración media (en minutos) de las sesiones de un grupo */
    @Query("""
           select avg(s.duracionMin)
           from Sesion s
           where s.grupo.id = :grupoId
           """)
    Double promedioDuracionPorGrupo(@Param("grupoId") Long grupoId);
}
