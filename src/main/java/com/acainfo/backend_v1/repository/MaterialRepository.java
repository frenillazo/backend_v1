package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    /* búsquedas básicas -------------------------------------------------- */
    List<Material> findByAsignaturaId(Long asignaturaId);
    List<Material> findByGrupoId(Long grupoId);           // opcional

    List<Material> findByTipoIgnoreCase(String tipo);
    Optional<Material> findByNombreAndAsignaturaId(String nombre, Long asignaturaId);
    boolean existsByNombreAndAsignaturaId(String nombre, Long asignaturaId);

    /* control de caducidad ---------------------------------------------- */
    List<Material> findByFechaExpiracionBefore(LocalDate limite);
}
