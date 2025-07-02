package com.acainfo.backend_v1.repository;

import com.acainfo.backend_v1.domain.Alumno;
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
 * Pruebas unitarias y de integración sobre {@link AlumnoRepository}.
 * <p>
 * Carga los datos definidos en <code>dataAlumnoRepository.sql</code> antes de cada test.
 * El script debe estar ubicado en <code>src/test/resources</code> para que la anotación
 * <code>@Sql</code> lo encuentre vía classpath.
 */
@DataJpaTest
@Sql(scripts = "/dataAlumnoRepository.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AlumnoRepositoryTest {

    @Autowired
    private AlumnoRepository alumnoRepository;

    /* --------------------------- CRUD básico --------------------------- */
    @Test
    @DisplayName("Crear, Leer, Actualizar y Borrar un Alumno")
    void crudTest() {
        long initialCount = alumnoRepository.count();

        Alumno nuevo = new Alumno();
        nuevo.setNombre("Test User");
        nuevo.setCarrera("Derecho");
        nuevo.setCurso("4");
        nuevo.setEmail("test@example.com");
        nuevo.setTelefono("677000000");

        // CREATE
        Alumno saved = alumnoRepository.save(nuevo);
        assertNotNull(saved.getId());

        // READ
        Optional<Alumno> buscado = alumnoRepository.findById(saved.getId());
        assertTrue(buscado.isPresent());
        assertEquals("Test User", buscado.get().getNombre());

        // UPDATE
        saved.setNombre("Test User Editado");
        Alumno actualizado = alumnoRepository.save(saved);
        assertEquals("Test User Editado", actualizado.getNombre());

        // DELETE
        alumnoRepository.delete(actualizado);
        assertEquals(initialCount, alumnoRepository.count());
    }

    /* ------------------- Métodos derivados (NO‑CRUD) ------------------- */
    @Test
    void findByCarrera() {
        List<Alumno> informatica = alumnoRepository.findByCarrera("Informática");
        assertThat(informatica).hasSize(3);
    }

    @Test
    void findByCarreraAndCurso() {
        List<Alumno> result = alumnoRepository.findByCarreraAndCurso("Informática", "1");
        assertThat(result).hasSize(2);
    }

    @Test
    void findByNombreContainingIgnoreCase() {
        List<Alumno> result = alumnoRepository.findByNombreContainingIgnoreCase("tor");
        assertThat(result).extracting(Alumno::getNombre).containsExactly("Juan Torres");
    }

    @Test
    void existsByEmail() {
        assertTrue(alumnoRepository.existsByEmail("ana@example.com"));
        assertFalse(alumnoRepository.existsByEmail("noexiste@example.com"));
    }

    @Test
    void existsByTelefono() {
        assertTrue(alumnoRepository.existsByTelefono("656789012"));
        assertFalse(alumnoRepository.existsByTelefono("000000000"));
    }

    @Test
    void findAllByGrupoId() {
        List<Alumno> grupo100 = alumnoRepository.findAllByGrupoId(100L);
        assertThat(grupo100).extracting(Alumno::getNombre)
                .containsExactlyInAnyOrder("Ana López", "Luis García");
    }

    @Test
    void findAlumnosSinGrupos() {
        List<Alumno> sinGrupo = alumnoRepository.findAlumnosSinGrupos();
        assertThat(sinGrupo).extracting(Alumno::getNombre)
                .containsExactlyInAnyOrder("Juan Torres", "Carla Núñez");
    }
}
