package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "materiales")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Material {

    /* ---------- PK ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- Propiedades ---------- */

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "Nombre máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "Tipo máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String tipo;           // vídeo, pdf, etc.

    @NotBlank(message = "La URL es obligatoria")
    @Size(max = 255)
    @Column(nullable = false, length = 255)
    private String url;

    /* Opcional: solo se valida si está presente */
    @Future(message = "La fecha de expiración debe ser futura")
    private LocalDate fechaExpiracion;

    /* ---------- Relaciones ---------- */

    /* Relación obligatoria con Asignatura */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "asignatura_id")
    private Asignatura asignatura;

    /* Relación opcional con Grupo */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id")
    private Grupo grupo;
}
