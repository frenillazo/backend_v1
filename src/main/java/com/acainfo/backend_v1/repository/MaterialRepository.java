package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long> {

    /* ---------- Filtros básicos ---------- */
    List<Material> findByTipoIgnoreCase(String tipo);
    List<Material> findByAsignaturaId(Long asignaturaId);
    List<Material> findByGrupoId(Long grupoId);
    List<Material> findByAsignaturaIdAndGrupoId(Long asignaturaId, Long grupoId);

    /* ---------- Gestión de URLs ---------- */
    boolean existsByUrl(String url);

    /* ---------- Estado de caducidad ---------- */

    /** Materiales vigentes (sin expiración o con expiración futura) */
    List<Material> findByFechaExpiracionIsNullOrFechaExpiracionAfter(LocalDate fecha);

    /** Materiales caducados hasta una fecha concreta */
    @Query("""
           select m
           from Material m
           where m.fechaExpiracion is not null
             and m.fechaExpiracion <= :fecha
           """)
    List<Material> findMaterialesCaducadosHasta(@Param("fecha") LocalDate fecha);

    /* ---------- Métricas ---------- */
    long countByTipoIgnoreCase(String tipo);
}

