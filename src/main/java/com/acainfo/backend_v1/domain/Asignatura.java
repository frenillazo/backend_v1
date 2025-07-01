package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "asignaturas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asignatura {

    /* ---------- PK ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- Propiedades ---------- */

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Nombre máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "La carrera es obligatoria")
    @Size(max = 50, message = "Carrera máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String carrera;

    /* ---------- Relaciones ---------- */

    /*
     * Un mismo material no debe aparecer dos veces en una asignatura.
     * Set garantiza unicidad lógica; @Builder.Default mantiene la
     * inicialización cuando se use el builder de Lombok.
     */
    @Builder.Default
    @OneToMany(mappedBy = "asignatura",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Material> materiales = new HashSet<>();
}