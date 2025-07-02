package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Asignatura;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias e integración sobre {@link AsignaturaRepository}.
 * <p>
 * Carga los datos definidos en <code>dataAsignaturaRepository.sql</code> antes de cada test.
 */
@DataJpaTest
@Sql(scripts = "/dataRepository.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AsignaturaRepositoryTest {

    @Autowired
    private AsignaturaRepository asignaturaRepository;

    /* --------------------------- CRUD básico --------------------------- */
    @Test
    @DisplayName("Crear, Leer, Actualizar y Borrar una Asignatura")
    void crudTest() {
        long initialCount = asignaturaRepository.count();

        Asignatura nueva = new Asignatura();
        nueva.setNombre("Teoría de Juegos");
        nueva.setCarrera("Economía");

        // CREATE
        Asignatura saved = asignaturaRepository.save(nueva);
        assertNotNull(saved.getId());

        // READ
        Optional<Asignatura> buscada =
                asignaturaRepository.findById(saved.getId());
        assertTrue(buscada.isPresent());
        assertEquals("Teoría de Juegos", buscada.get().getNombre());

        // UPDATE
        saved.setNombre("Teoría de Juegos Avanzada");
        Asignatura actualizada = asignaturaRepository.save(saved);
        assertEquals("Teoría de Juegos Avanzada",
                     actualizada.getNombre());

        // DELETE
        asignaturaRepository.delete(actualizada);
        assertEquals(initialCount, asignaturaRepository.count());
    }

    /* ------------------- Métodos derivados (NO-CRUD) ------------------- */

    @Test
    void findByNombreAndCarrera() {
        Optional<Asignatura> progI =
                asignaturaRepository.findByNombreAndCarrera(
                        "Programación I", "Informática");
        assertTrue(progI.isPresent());
    }

    @Test
    void findByCarreraOrderByNombreAsc() {
        List<Asignatura> informatica =
                asignaturaRepository.findByCarreraOrderByNombreAsc(
                        "Informática");
        assertThat(informatica)
                .extracting(Asignatura::getNombre)
                .containsExactly("Bases de Datos", "Programación I"); // orden asc.
    }

    @Test
    void existsByNombreAndCarrera() {
        assertTrue(asignaturaRepository
                .existsByNombreAndCarrera("Programación I", "Informática"));
        assertFalse(asignaturaRepository
                .existsByNombreAndCarrera("No Existe", "Informática"));
    }

    @Test
    void countByCarrera() {
        assertEquals(2, asignaturaRepository.countByCarrera("Informática"));
    }

    @Test
    void findCarrerasDisponibles() {
        List<String> carreras = asignaturaRepository.findCarrerasDisponibles();
        assertThat(carreras)
                .containsExactly("Filología", "Historia", "Informática", "Medicina");
    }

    @Test
    void findAsignaturasSinMaterial() {
        List<Asignatura> sinMaterial =
                asignaturaRepository.findAsignaturasSinMaterial();
        assertThat(sinMaterial)
                .extracting(Asignatura::getNombre)
                .containsExactlyInAnyOrder(
                        "Anatomía", "Historia Antigua", "Literatura Universal");
    }
}
