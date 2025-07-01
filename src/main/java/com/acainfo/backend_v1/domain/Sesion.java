package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

@Entity
@Table(name = "sesiones")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Sesion {

    /* ---------- PK ---------- */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- Propiedades ---------- */

    @NotBlank(message = "El día de la semana es obligatorio")
    @Pattern(regexp = "(?i)Lunes|Martes|Miércoles|Miercoles|Jueves|Viernes|Sábado|Sabado|Domingo",
             message = "Día de la semana no válido")
    @Column(nullable = false, length = 9)
    private String diaSemana;       // Lunes, Martes...

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    @NotNull(message = "La duración es obligatoria")
    @Positive(message = "La duración debe ser positiva")
    private Integer duracionMin;    // minutos

    @Size(max = 255, message = "Descripción máxima 255 caracteres")
    private String descripcion;

    /* ---------- Relaciones ---------- */

    /* Muchas sesiones → 1 grupo */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
}

