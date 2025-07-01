package com.acainfo.backend_v1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;

import com.acainfo.backend_v1.repository.AsignaturaRepository;
import com.acainfo.backend_v1.repository.MaterialRepository;
import com.acainfo.backend_v1.repository.GrupoRepository;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
class MaterialMappingTest {

    @Autowired private AsignaturaRepository asignaturaRepo;
    @Autowired private MaterialRepository materialRepo;
    @Autowired private GrupoRepository grupoRepo;

    @Test
    @DisplayName("Guarda y recupera un material con asignatura y grupo opcional")
    void persistAndLoadMaterial() {
        // Asignatura obligatoria
        Asignatura asig = asignaturaRepo.save(Asignatura.builder()
                .nombre("Álgebra")
                .carrera("Ingeniería")
                .build());

        // Grupo opcional
        Grupo g = grupoRepo.save(Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 1, 10))
                .fechaFin(LocalDate.of(2025, 5, 30))
                .build());

        Material mat = Material.builder()
                .nombre("Tema 1")
                .tipo("pdf")
                .url("https://acainfo.es/alg/t1.pdf")
                .fechaExpiracion(LocalDate.now().plusMonths(6))
                .asignatura(asig)
                .grupo(g)
                .build();

        Material guardado = materialRepo.save(mat);
        Material recuperado = materialRepo.findById(guardado.getId()).orElseThrow();

        assertThat(recuperado.getAsignatura().getNombre()).isEqualTo("Álgebra");
        assertThat(recuperado.getGrupo().getId()).isEqualTo(g.getId());
    }

    @Test
    @DisplayName("La fecha de expiración debe ser futura")
    void pastExpirationDateNotAllowed() {
        Asignatura asig = asignaturaRepo.save(Asignatura.builder()
                .nombre("Programación")
                .carrera("Informática")
                .build());

        Material mat = Material.builder()
                .nombre("Ejemplo")
                .tipo("video")
                .url("https://acainfo.es/prog/video.mp4")
                .fechaExpiracion(LocalDate.now().minusDays(1)) // pasada
                .asignatura(asig)
                .build();

        assertThatThrownBy(() -> materialRepo.saveAndFlush(mat))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
