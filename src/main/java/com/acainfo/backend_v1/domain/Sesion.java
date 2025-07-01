package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "sesiones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sesion {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String diaSemana;       // Lunes, Martes...
    private LocalTime horaInicio;
    private Integer duracionMin;    // minutos totales
    private String descripcion;

    /* Muchas sesiones → 1 grupo */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
}
