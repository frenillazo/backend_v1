package com.acainfo.backend_v1.domain;

import com.acainfo.backend_v1.repository.AlumnoRepository;
import com.acainfo.backend_v1.repository.AsignaturaRepository;
import com.acainfo.backend_v1.repository.GrupoRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprueba el mapeo de Grupo con Asignatura, Inscripciones y Sesiones.
 */
@DataJpaTest
class GrupoMappingTest {

    @Autowired private GrupoRepository grupoRepo;
    @Autowired private AsignaturaRepository asignaturaRepo;
    @Autowired private AlumnoRepository alumnoRepo;

    @Test
    @DisplayName("Persiste y recupera un grupo con asignatura, inscripciones y sesiones")
    void persistAndLoadGrupoWithRelations() {
        // ---------- 1. Asignatura previa (FK obligatoria) ----------
        Asignatura anatomia = Asignatura.builder()
                .nombre("Anatomía")
                .carrera("Medicina")
                .build();
        asignaturaRepo.save(anatomia);

        // ---------- 2. Alumno que se inscribe ----------
        Alumno al = Alumno.builder()
                .nombre("María")
                .carrera("Medicina")
                .curso("3º")
                .email("maria@uni.es")
                .telefono("622334455")
                .build();
        alumnoRepo.save(al);

        // ---------- 3. Grupo + relaciones ----------
        Grupo g = Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 2, 1))
                .fechaFin(LocalDate.of(2025, 5, 31))
                .asignatura(anatomia)    // nuevo campo
                .build();

        Inscripcion ins = Inscripcion.builder()
                .fechaInscripcion(LocalDate.now())
                .alumno(al)
                .grupo(g)
                .build();
        g.getInscripciones().add(ins);

        Sesion s1 = Sesion.builder()
                .diaSemana("Lunes")
                .horaInicio(LocalTime.of(10, 0))
                .duracionMin(120)
                .descripcion("Teoría y prácticas")
                .grupo(g)
                .build();
        g.getSesiones().add(s1);

        // ---------- 4. Persistencia y verificación ----------
        Grupo guardado = grupoRepo.save(g);
        Grupo recuperado = grupoRepo.findById(guardado.getId()).orElseThrow();

        assertThat(recuperado.getAsignatura().getNombre()).isEqualTo("Anatomía");
        assertThat(recuperado.getInscripciones()).hasSize(1);
        assertThat(recuperado.getSesiones()).hasSize(1);
    }

    @Test
    @DisplayName("Valida que fechaFin sea posterior a fechaInicio")
    void invalidDateRange() {
        Asignatura mate = asignaturaRepo.save(
                Asignatura.builder()
                          .nombre("Matemáticas")
                          .carrera("Ingeniería")
                          .build());

        Grupo g = Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 9, 1))
                .fechaFin(LocalDate.of(2025, 8, 1)) // anterior
                .asignatura(mate)
                .build();

        assertThatThrownBy(() -> grupoRepo.saveAndFlush(g))
                .isInstanceOf(ConstraintViolationException.class);
    }
}

