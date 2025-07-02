-- dataRepository.sql   (H2 compatible)
-- Carga conjunta de datos de prueba para TODOS los repositorios
-- (asignatura, alumno, grupo, material, inscripción, …)

/* ==========================================================
   1.  Limpiar tablas – sin romper FKs
   ========================================================== */
SET REFERENTIAL_INTEGRITY FALSE;

TRUNCATE TABLE inscripciones;
TRUNCATE TABLE materiales;
TRUNCATE TABLE sesiones;
TRUNCATE TABLE grupos;
TRUNCATE TABLE alumnos;
TRUNCATE TABLE asignaturas;

SET REFERENTIAL_INTEGRITY TRUE;

/* ==========================================================
   2.  Datos básicos de ASIGNATURAS + MATERIALES
   ========================================================== */
INSERT INTO asignaturas (id, nombre, carrera) VALUES
    (10, 'Programación I',       'Informática'),
    (11, 'Bases de Datos',       'Informática'),
    (12, 'Anatomía',             'Medicina'),
    (13, 'Historia Antigua',     'Historia'),
    (14, 'Literatura Universal', 'Filología');

-- Materiales sólo para las dos primeras (sirve a findAsignaturasSinMaterial)
INSERT INTO materiales
       (id, nombre, tipo, url, fecha_expiracion, asignatura_id, grupo_id)
VALUES (200, 'Apuntes Programación', 'pdf',
              'https://ejemplo.com/prog.pdf', DATE '2026-01-01', 10, NULL),
       (201, 'Vídeo BD',             'video',
              'https://ejemplo.com/bd.mp4',   DATE '2026-01-01', 11, NULL);

/* ==========================================================
   3.  Grupos de prueba (NECESITAN asignatura_id ≠ NULL)
   ========================================================== */
INSERT INTO grupos (id, fecha_inicio, fecha_fin, asignatura_id) VALUES
    (100, DATE '2025-01-10', DATE '2025-06-10', 10),  -- Programación I
    (101, DATE '2025-02-15', DATE '2025-07-15', 11);  -- Bases de Datos

/* ==========================================================
   4.  Alumnos e Inscripciones
   ========================================================== */
INSERT INTO alumnos (id, nombre, carrera, curso, email, telefono, foto) VALUES
    (1, 'Ana López',   'Informática', '1', 'ana@example.com',  '612345678', NULL),
    (2, 'Luis García', 'Informática', '2', 'luis@example.com', '623456789', NULL),
    (3, 'María Pérez', 'Medicina',    '1', 'maria@example.com','634567890', NULL),
    (4, 'Juan Torres', 'Informática', '1', 'juan@example.com', '645678901', NULL),
    (5, 'Carla Núñez', 'Historia',    '3', 'carla@example.com','656789012', NULL);

INSERT INTO inscripciones (id, fecha_inscripcion, alumno_id, grupo_id) VALUES
    (1000, DATE '2025-02-01', 1, 100),  -- Ana  -> Grupo 100
    (1001, DATE '2025-02-02', 2, 100),  -- Luis -> Grupo 100
    (1002, DATE '2025-03-01', 3, 101);  -- María-> Grupo 101

/* ==========================================================
   5.  Reiniciar identidades para evitar colisiones en tests
   ========================================================== */
ALTER TABLE asignaturas  ALTER COLUMN id RESTART WITH 100;
ALTER TABLE grupos       ALTER COLUMN id RESTART WITH 200;
ALTER TABLE alumnos      ALTER COLUMN id RESTART WITH 10;
ALTER TABLE materiales   ALTER COLUMN id RESTART WITH 300;
ALTER TABLE inscripciones ALTER COLUMN id RESTART WITH 2000;