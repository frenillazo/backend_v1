package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalTime;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

    List<Sesion> findByGrupoId(Long grupoId);


    List<Sesion> findByDiaSemanaIgnoreCase(String diaSemana);
    List<Sesion> findByGrupoIdAndDiaSemanaIgnoreCase(Long grupoId, String diaSemana);

    @Query("""
           select s
           from Sesion s
           where s.grupo.id = :grupoId
             and s.horaInicio between :inicio and :fin
           """)
    List<Sesion> findByGrupoIdAndHoraInicioBetween(Long grupoId,
                                                   LocalTime inicio,
                                                   LocalTime fin);

    long countByGrupoId(Long grupoId);
}
