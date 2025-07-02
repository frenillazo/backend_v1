
-- dataAlumnoRepository.sql (H2 compatible)
-- Script de carga de datos de prueba para AlumnoRepository tests
-- Adaptado para H2: usa SET REFERENTIAL_INTEGRITY en lugar de FOREIGN_KEY_CHECKS.

-- === Desactivar integridad referencial temporalmente ===
SET REFERENTIAL_INTEGRITY FALSE;

-- Limpiar tablas (si existen)
TRUNCATE TABLE inscripciones;
TRUNCATE TABLE alumnos;
TRUNCATE TABLE grupos;

-- === Reactivar integridad referencial ===
SET REFERENTIAL_INTEGRITY TRUE;

-- === Inserción de Alumnos ===
INSERT INTO alumnos (id, nombre, carrera, curso, email, telefono, foto) VALUES
    (1, 'Ana López',        'Informática', '1', 'ana@example.com',   '612345678', NULL),
    (2, 'Luis García',      'Informática', '2', 'luis@example.com',  '623456789', NULL),
    (3, 'María Pérez',      'Medicina',    '1', 'maria@example.com', '634567890', NULL),
    (4, 'Juan Torres',      'Informática', '1', 'juan@example.com',  '645678901', NULL),
    (5, 'Carla Núñez',      'Historia',    '3', 'carla@example.com', '656789012', NULL);

-- === Inserción de Grupos ===
INSERT INTO grupos (id, fecha_inicio, fecha_fin) VALUES
    (100, DATE '2025-01-10', DATE '2025-06-10'),
    (101, DATE '2025-02-15', DATE '2025-07-15');

-- === Inserción de Inscripciones ===
INSERT INTO inscripciones (id, fecha_inscripcion, alumno_id, grupo_id) VALUES
    (1000, DATE '2025-02-01', 1, 100),  -- Ana López  -> Grupo 100
    (1001, DATE '2025-02-02', 2, 100),  -- Luis García -> Grupo 100
    (1002, DATE '2025-03-01', 3, 101);  -- María Pérez -> Grupo 101

-- ==========
-- Resultados esperados para los tests:
--   findByCarrera('Informática')                  -> 3 registros
--   findByCarreraAndCurso('Informática','1')      -> 2 registros
--   findByNombreContainingIgnoreCase('lo')        -> 1 registro (Ana López)
--   existsByEmail('ana@example.com')              -> true
--   existsByTelefono('656789012')                 -> true
--   findAllByGrupoId(100)                         -> Ana López, Luis García
--   findAlumnosSinGrupos()                        -> Juan Torres, Carla Núñez
-- ==========
