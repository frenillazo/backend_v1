package com.acainfo.backend_v1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import jakarta.validation.ConstraintViolationException;

import com.acainfo.backend_v1.repository.AlumnoRepository;
import com.acainfo.backend_v1.repository.GrupoRepository;
import com.acainfo.backend_v1.repository.InscripcionRepository;
import com.acainfo.backend_v1.repository.AsignaturaRepository;


import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class InscripcionMappingTest {

    @Autowired private AlumnoRepository alumnoRepo;
    @Autowired private GrupoRepository grupoRepo;
    @Autowired private InscripcionRepository inscripcionRepo;
    @Autowired private AsignaturaRepository asignaturaRepo;

    @Test
    @DisplayName("Guarda y recupera una inscripción válida")
    void persistAndLoadInscripcion() {
        Alumno al = Alumno.builder()
                .nombre("Luis")
                .carrera("Historia")
                .curso("1º")
                .email("luis@uni.es")
                .telefono("699887766")
                .build();
        alumnoRepo.save(al);

        Asignatura fisica = asignaturaRepo.save(
                Asignatura.builder()
                        .nombre("Física")
                        .carrera("Ingeniería")
                        .build());

        Grupo g = grupoRepo.save(Grupo.builder()
                .fechaInicio(LocalDate.of(2025,2,1))
                .fechaFin(LocalDate.of(2025,6,30))
                .asignatura(fisica)
                .build());

        Inscripcion ins = Inscripcion.builder()
                .fechaInscripcion(LocalDate.now())
                .alumno(al)
                .grupo(g)
                .build();
        inscripcionRepo.save(ins);

        Inscripcion recuperada = inscripcionRepo.findById(ins.getId()).orElseThrow();
        assertThat(recuperada.getAlumno().getEmail()).isEqualTo("luis@uni.es");
        assertThat(recuperada.getGrupo().getId()).isEqualTo(g.getId());
    }

    @Test
    @DisplayName("No permite dos inscripciones duplicadas (alumno + grupo)")
    void uniqueAlumnoGrupo() {
        Alumno al = alumnoRepo.save(Alumno.builder()
                .nombre("Sara")
                .carrera("Física")
                .curso("2º")
                .email("sara@uni.es")
                .telefono("633221100")
                .build());

        Asignatura fisica = asignaturaRepo.save(
                Asignatura.builder()
                        .nombre("Física")
                        .carrera("Ingeniería")
                        .build());

        Grupo g = grupoRepo.save(Grupo.builder()
                .fechaInicio(LocalDate.of(2025,2,1))
                .fechaFin(LocalDate.of(2025,6,30))
                .asignatura(fisica)
                .build());

        Inscripcion ins1 = Inscripcion.builder()
                .fechaInscripcion(LocalDate.now())
                .alumno(al)
                .grupo(g)
                .build();
        Inscripcion ins2 = Inscripcion.builder()
                .fechaInscripcion(LocalDate.now())
                .alumno(al)
                .grupo(g) // Mismas PKs compuestas
                .build();

        inscripcionRepo.save(ins1);
        assertThatThrownBy(() -> inscripcionRepo.saveAndFlush(ins2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("La fecha de inscripción no puede ser futura")
    void futureDateNotAllowed() {
        Alumno al = alumnoRepo.save(Alumno.builder()
                .nombre("Tomás")
                .carrera("Química")
                .curso("3º")
                .email("tomas@uni.es")
                .telefono("677445566")
                .build());
        Asignatura fisica = asignaturaRepo.save(
                Asignatura.builder()
                        .nombre("Física")
                        .carrera("Ingeniería")
                        .build());

        Grupo g = grupoRepo.save(Grupo.builder()
                .fechaInicio(LocalDate.of(2025,2,1))
                .fechaFin(LocalDate.of(2025,6,30))
                .asignatura(fisica)
                .build());

        Inscripcion ins = Inscripcion.builder()
                .fechaInscripcion(LocalDate.now().plusDays(1)) // futura
                .alumno(al)
                .grupo(g)
                .build();

        assertThatThrownBy(() -> inscripcionRepo.saveAndFlush(ins))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
