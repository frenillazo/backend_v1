package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    /* PK compuesta lógicamente (UNIQUE alumno+grupo) -------------------- */
    Optional<Inscripcion> findByAlumnoIdAndGrupoId(Long alumnoId, Long grupoId);
    boolean existsByAlumnoIdAndGrupoId(Long alumnoId, Long grupoId);

    /* navegación -------------------------------------------------------- */
    List<Inscripcion> findByAlumnoId(Long alumnoId);
    List<Inscripcion> findByGrupoId(Long grupoId);

    /* agregados --------------------------------------------------------- */
    long countByGrupoId(Long grupoId);
}

