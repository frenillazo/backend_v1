package com.acainfo.backend_v1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;


import com.acainfo.backend_v1.repository.AlumnoRepository;
import com.acainfo.backend_v1.repository.AsignaturaRepository;
import com.acainfo.backend_v1.repository.GrupoRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class AlumnoMappingTest {

    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private AsignaturaRepository asignaturaRepo;
    @Autowired private GrupoRepository grupoRepo;

    @Test
    @DisplayName("Guarda y recupera un alumno con su inscripción")
    void persistAndLoadAlumnoWithInscripcion() {
        // 1) Creamos y persistimos un grupo válido
        Asignatura mates = asignaturaRepo.save(
            Asignatura.builder()
                    .nombre("Matemáticas")
                    .carrera("Ingeniería")
                    .build());
        Grupo g = Grupo.builder()
            .fechaInicio(LocalDate.of(2025, 9, 1))
            .fechaFin(LocalDate.of(2026, 1, 31))
            .asignatura(mates)   // <-- FK añadida
            .build();
        grupoRepo.save(g);

        // 2) Creamos el alumno y su inscripción asociada
        Alumno al = Alumno.builder()
                .nombre("Carlos")
                .carrera("Informática")
                .curso("4º")
                .email("carlos@uni.es")
                .telefono("612345678")
                .build();
        Inscripcion ins = Inscripcion.builder()
                .fechaInscripcion(LocalDate.now())
                .alumno(al)
                .grupo(g)
                .build();
        al.getInscripciones().add(ins);

        // 3) Persistimos y recuperamos
        Alumno guardado = alumnoRepo.save(al);
        Alumno recuperado = alumnoRepo.findById(guardado.getId()).orElseThrow();

        // 4) Aserciones
        assertThat(recuperado.getNombre()).isEqualTo("Carlos");
        assertThat(recuperado.getInscripciones()).hasSize(1);
        Inscripcion recuperada = recuperado.getInscripciones().iterator().next();
        assertThat(recuperada.getGrupo().getId()).isEqualTo(g.getId());
    }

    @Test
    @DisplayName("No permite dos alumnos con e‑mail idéntico")
    void uniqueEmailConstraint() {
        Alumno a1 = Alumno.builder()
                .nombre("Ana")
                .carrera("Derecho")
                .curso("1º")
                .email("dup@acainfo.es")
                .telefono("712345678")
                .build();

        Alumno a2 = Alumno.builder()
                .nombre("Antonio")
                .carrera("Medicina")
                .curso("2º")
                .email("dup@acainfo.es")  // Mismo e‑mail
                .telefono("912345678")
                .build();

        alumnoRepo.save(a1);
        assertThatThrownBy(() -> alumnoRepo.saveAndFlush(a2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

