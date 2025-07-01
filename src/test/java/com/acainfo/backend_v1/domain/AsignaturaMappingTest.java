// src/test/java/com/acainfo/backend_v1/domain/AsignaturaMappingTest.java
package com.acainfo.backend_v1.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.acainfo.backend_v1.repository.AsignaturaRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest            // Arranca solo la capa JPA con una BD H2 en memoria
class AsignaturaMappingTest {

    @Autowired
    private AsignaturaRepository asignaturaRepo;     // Interface que extiende JpaRepository

    @Test
    @DisplayName("Guarda y recupera una asignatura con sus materiales")
    void persistAndLoadAsignaturaWithMaterials() {
        // 1) Creamos la entidad y su material asociado
        Asignatura algebra = Asignatura.builder()
                .nombre("Álgebra Lineal")
                .carrera("Ingeniería")
                .build();

        Material pdf = Material.builder()
                .nombre("Apuntes Tema 1")
                .tipo("pdf")
                .url("https://acainfo.es/algebra/t1.pdf")
                .asignatura(algebra)      // Relación inversa
                .build();

        algebra.getMateriales().add(pdf);

        // 2) Persistimos y vaciamos la sesión (flush & clear implícitos en @DataJpaTest)
        Asignatura guardada = asignaturaRepo.save(algebra);

        // 3) Recuperamos desde la BD
        Asignatura recuperada = asignaturaRepo.findById(guardada.getId()).orElseThrow();
        List<Material> mats = recuperada.getMateriales().stream().toList();

        // 4) Aserciones
        assertThat(recuperada.getNombre()).isEqualTo("Álgebra Lineal");
        assertThat(mats).hasSize(1);
        assertThat(mats.get(0).getUrl()).contains(".pdf");
    }

    @Test
    @DisplayName("No permite dos asignaturas con nombre idéntico y carrera igual")
    void uniqueConstraint() {
        Asignatura a1 = Asignatura.builder()
                .nombre("Programación")
                .carrera("Informática")
                .build();

        Asignatura a2 = Asignatura.builder()
                .nombre("Programación")   // Misma combinación
                .carrera("Informática")
                .build();

        asignaturaRepo.save(a1);
        // flush() fuerza la ejecución del INSERT → debe fallar
        assertThatThrownBy(() -> asignaturaRepo.saveAndFlush(a2))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}

