package com.acainfo.backend_v1.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*; 
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "alumnos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Alumno {

    /* ---------- PK ---------- */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /* ---------- Datos personales ---------- */

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 80, message = "Nombre máximo 80 caracteres")
    private String nombre;

    @NotBlank(message = "La carrera es obligatoria")
    @Size(max = 80)
    private String carrera;

    @NotBlank(message = "El curso es obligatorio")
    @Size(max = 10)
    private String curso;

    @Email(message = "Formato de e-mail no válido")
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    /* 
     * Patrón para móviles españoles:
     *   – Empieza por 6, 7 o 9
     *   – Nueve dígitos en total
     */
    @Pattern(regexp = "^[679]\\d{8}$",
             message = "Teléfono debe ser un móvil español válido")
    private String telefono;

    /* No lo marcamos obligatorio: la foto puede ser null */
    private String foto;

    /* ---------- Relaciones ---------- */

    /*
     * Set evita duplicados; @Builder.Default mantiene la inicialización
     * cuando se use el builder de Lombok.
     */
    @Builder.Default
    @OneToMany(mappedBy = "alumno",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    private Set<Inscripcion> inscripciones = new HashSet<>();
}

