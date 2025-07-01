package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "materiales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String tipo;           // vídeo, pdf, etc.
    private String url;
    private LocalDate fechaExpiracion;

    /* Relación obligatoria con Asignatura */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    /* Relación opcional con Grupo (puede ser null) */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
}
