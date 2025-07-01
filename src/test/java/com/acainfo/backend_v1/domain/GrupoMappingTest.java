package com.acainfo.backend_v1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;

import com.acainfo.backend_v1.repository.GrupoRepository;
import com.acainfo.backend_v1.repository.AlumnoRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;
@DataJpaTest
class GrupoMappingTest {

    @Autowired private GrupoRepository grupoRepo;
    @Autowired private AlumnoRepository alumnoRepo;

    @Test
    @DisplayName("Guarda y recupera un grupo con inscripciones y sesiones")
    void persistAndLoadGrupoWithRelations() {
        // Alumno que se inscribirá
        Alumno al = Alumno.builder()
                .nombre("María")
                .carrera("Biología")
                .curso("3º")
                .email("maria@uni.es")
                .telefono("622334455")
                .build();
        alumnoRepo.save(al);

        // Grupo con rango de fechas válido
        Grupo g = Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 2, 1))
                .fechaFin(LocalDate.of(2025, 5, 31))
                .build();

        // Inscripción y sesión
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

        // Persistimos grupo (cascada → inscripciones y sesiones)
        Grupo guardado = grupoRepo.save(g);
        Grupo recuperado = grupoRepo.findById(guardado.getId()).orElseThrow();

        assertThat(recuperado.getInscripciones()).hasSize(1);
        assertThat(recuperado.getSesiones()).hasSize(1);
    }

    @Test
    @DisplayName("Valida que fechaFin sea posterior a fechaInicio")
    void invalidDateRange() {
        Grupo g = Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 9, 1))
                .fechaFin(LocalDate.of(2025, 8, 1)) // < fechaInicio
                .build();

        assertThatThrownBy(() -> grupoRepo.saveAndFlush(g))
                .isInstanceOf(ConstraintViolationException.class);
    }
}

