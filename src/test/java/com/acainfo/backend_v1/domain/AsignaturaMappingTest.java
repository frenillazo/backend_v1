package com.acainfo.backend_v1.domain;

import com.acainfo.backend_v1.repository.AsignaturaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

/**
 * Comprueba el mapeo de Asignatura con Materiales y Grupos.
 */
@DataJpaTest
class AsignaturaMappingTest {

    @Autowired private AsignaturaRepository asignaturaRepo;

    @Test
    @DisplayName("Persiste y recupera una asignatura con materiales y grupos")
    void persistAndLoadAsignaturaWithRelations() {
        // ---------- 1. Creamos la asignatura ----------
        Asignatura algebra = Asignatura.builder()
                .nombre("Álgebra Lineal")
                .carrera("Ingeniería")
                .build();

        // ---------- 2. Material asociado ----------
        Material pdf = Material.builder()
                .nombre("Apuntes Tema 1")
                .tipo("pdf")
                .url("https://acainfo.es/algebra/t1.pdf")
                .asignatura(algebra)
                .build();
        algebra.getMateriales().add(pdf);

        // ---------- 3. Grupo asociado ----------
        Grupo grupoMañanas = Grupo.builder()
                .fechaInicio(LocalDate.of(2025, 9, 1))
                .fechaFin(LocalDate.of(2026, 1, 31))
                .asignatura(algebra)           // FK obligatoria
                .build();
        algebra.getGrupos().add(grupoMañanas);

        // ---------- 4. Persistencia en cascada ----------
        Asignatura guardada = asignaturaRepo.save(algebra);

        // ---------- 5. Recuperación y aserciones ----------
        Asignatura recuperada = asignaturaRepo
                .findById(guardada.getId()).orElseThrow();

        assertThat(recuperada.getMateriales()).hasSize(1);
        assertThat(recuperada.getGrupos()).hasSize(1);
        assertThat(recuperada.getGrupos().iterator().next()
                              .getAsignatura().getNombre())
                .isEqualTo("Álgebra Lineal");
    }

    @Test
    @DisplayName("No permite duplicar (nombre,carrera)")
    void uniqueConstraint() {
        Asignatura a1 = Asignatura.builder()
                .nombre("Programación")
                .carrera("Informática")
                .build();
        Asignatura a2 = Asignatura.builder()
                .nombre("Programación")
                .carrera("Informática")
                .build();

        asignaturaRepo.save(a1);
        assertThatThrownBy(() -> asignaturaRepo.saveAndFlush(a2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
