package com.acainfo.backend_v1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;

import com.acainfo.backend_v1.repository.SesionRepository;
import com.acainfo.backend_v1.repository.GrupoRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class SesionMappingTest {

    @Autowired private SesionRepository sesionRepo;
    @Autowired private GrupoRepository grupoRepo;

    @Test
    @DisplayName("Guarda y recupera una sesión válida")
    void persistAndLoadSesion() {
        Grupo g = grupoRepo.save(Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 2, 1))
                .fechaFin(LocalDate.of(2025, 4, 30))
                .build());

        Sesion s = Sesion.builder()
                .diaSemana("Miércoles")
                .horaInicio(LocalTime.of(8, 30))
                .duracionMin(90)
                .descripcion("Teoría")
                .grupo(g)
                .build();

        Sesion guardada = sesionRepo.save(s);
        Sesion recuperada = sesionRepo.findById(guardada.getId()).orElseThrow();
        assertThat(recuperada.getDiaSemana()).isEqualToIgnoringCase("Miércoles");
        assertThat(recuperada.getGrupo().getId()).isEqualTo(g.getId());
    }

    @Test
    @DisplayName("Día de la semana debe cumplir el patrón")
    void invalidDayOfWeek() {
        Grupo g = grupoRepo.save(Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 2, 1))
                .fechaFin(LocalDate.of(2025, 4, 30))
                .build());

        Sesion s = Sesion.builder()
                .diaSemana("Funday") // inválido
                .horaInicio(LocalTime.of(10, 0))
                .duracionMin(60)
                .grupo(g)
                .build();

        assertThatThrownBy(() -> sesionRepo.saveAndFlush(s))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("La duración debe ser positiva")
    void negativeDurationNotAllowed() {
        Grupo g = grupoRepo.save(Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 2, 1))
                .fechaFin(LocalDate.of(2025, 4, 30))
                .build());

        Sesion s = Sesion.builder()
                .diaSemana("Jueves")
                .horaInicio(LocalTime.of(9, 0))
                .duracionMin(-15) // negativo
                .grupo(g)
                .build();

        assertThatThrownBy(() -> sesionRepo.saveAndFlush(s))
                .isInstanceOf(ConstraintViolationException.class);
    }
}

