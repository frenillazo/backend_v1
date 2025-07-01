package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alumnos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alumno {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String carrera;
    private String curso;

    @Column(nullable = false, unique = true)
    private String email;

    private String telefono;
    private String foto;

    /* 
     * Se usa Set porque:
     * 1) Una inscripción no debe repetirse para el mismo alumno-grupo.
     * 2) HashSet ofrece comprobación de duplicados O(1).
     * 3) El orden no aporta significado de dominio.
     */
    @Builder.Default
    @OneToMany(mappedBy = "alumno", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Inscripcion> inscripciones = new HashSet<>();
}

