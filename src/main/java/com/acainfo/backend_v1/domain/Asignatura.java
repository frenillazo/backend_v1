package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "asignaturas")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Asignatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String carrera;

    /* 
     * Un mismo material no debe aparecer dos veces en una asignatura,
     * de modo que Set garantiza unicidad lógica.
     */
    @Builder.Default
    @OneToMany(mappedBy = "asignatura", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Material> materiales = new HashSet<>();
}
