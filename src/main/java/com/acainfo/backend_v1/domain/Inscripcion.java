package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "inscripciones", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"alumno_id", "grupo_id"})
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fechaInscripcion;

    /* Muchas inscripciones → 1 alumno */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "alumno_id")
    private Alumno alumno;

    /* Muchas inscripciones → 1 grupo */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
}
