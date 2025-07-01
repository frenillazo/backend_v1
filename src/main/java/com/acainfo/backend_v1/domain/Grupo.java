package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "grupos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Grupo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    /* Alumnos únicos en el grupo */
    @Builder.Default
    @OneToMany(mappedBy = "grupo",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Inscripcion> inscripciones = new HashSet<>();

    /* Materiales opcionales, también sin duplicados */
    @Builder.Default
    @OneToMany(mappedBy = "grupo",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Material> materiales = new HashSet<>();

    /* Plantillas de sesión (SESION) asociadas al grupo */
    @Builder.Default
    @OneToMany(mappedBy = "grupo",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Sesion> sesiones = new HashSet<>();
}
