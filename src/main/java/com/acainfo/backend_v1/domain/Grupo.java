package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grupos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Grupo {

    /* ---------- PK ---------- */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- Propiedades ---------- */

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDate fechaFin;

    /*
     * Garantiza que la fechaFin sea posterior a fechaInicio.
     * Se permite null al crear la entidad con el builder y
     * posteriormente asignar valores.
     */
    @AssertTrue(message = "La fecha de fin debe ser posterior a la de inicio")
    private boolean isRangoFechasValido() {
        return fechaInicio == null || fechaFin == null || fechaFin.isAfter(fechaInicio);
    }

    /* ---------- Relaciones ---------- */

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignatura_id", nullable = false)
    private Asignatura asignatura;

    /* Alumnos únicos en el grupo */
    @Builder.Default
    @OneToMany(mappedBy = "grupo",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Inscripcion> inscripciones = new HashSet<>();

    /* Materiales opcionales */
    @Builder.Default
    @OneToMany(mappedBy = "grupo",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Material> materiales = new HashSet<>();

    /* Sesiones (plantillas) asociadas */
    @Builder.Default
    @OneToMany(mappedBy = "grupo",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Sesion> sesiones = new HashSet<>();

}