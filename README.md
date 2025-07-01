Implementación con spring boot sobre el siguiente esquema inicial
ENTIDAD: ALUMNO
─────────────────────────────────────────────────────
PK  id_alumno       : BIGINT
    nombre          : VARCHAR(100)
    carrera         : VARCHAR(50)
    curso           : VARCHAR(20)
    email           : VARCHAR(100) UNIQUE NOT NULL
    teléfono        : VARCHAR(20)
    foto            : VARCHAR(255)


ENTIDAD: ASIGNATURA
─────────────────────────────────────────────────────
PK  id_asignatura   : BIGINT
    nombre          : VARCHAR(100)
    carrera         : VARCHAR(50)
    curso           : VARCHAR(20)
    cuatrimestre    : SMALLINT       — 1 ó 2
    precio_mes      : DECIMAL(8,2)


ENTIDAD: GRUPO
─────────────────────────────────────────────────────
PK  id_grupo        : BIGINT
    nombre_grupo    : VARCHAR(50)    — p.ej. 'GR1', 'GR2'
FK  asignatura_id   : BIGINT → ASIGNATURA.id_asignatura  
    — Cada grupo es una variante de horario de una asignatura


ENTIDAD: SESION
─────────────────────────────────────────────────────
PK  id_sesion       : BIGINT
    dia_semana      : ENUM('LUN','MAR','MIE','JUE','VIE','SAB','DOM')
    hora_inicio     : TIME           — p.ej. '08:00'
    duracion        : INTERVAL       — p.ej. '00:45'
FK  grupo_id        : BIGINT → GRUPO.id_grupo  (ON DELETE CASCADE)
    — Cada sesión corresponde a un encuentro concreto de un grupo


ENTIDAD: INSCRIPCION
─────────────────────────────────────────────────────
PK  id_inscripcion  : BIGINT
    fecha_inicio    : DATE
FK  alumno_id       : BIGINT → ALUMNO.id_alumno  (ON DELETE CASCADE)
FK  grupo_id        : BIGINT → GRUPO.id_grupo     (ON DELETE CASCADE)
    — Inscribe al alumno en un grupo concreto


ENTIDAD: MATERIAL
─────────────────────────────────────────────────────
PK  id_material     : BIGINT
    tipo            : VARCHAR(50)    — p.ej. ‘vídeo’, ‘pdf’
    url             : VARCHAR(255)
    fecha_expiracion: DATE           — (opcional)
FK  asignatura_id   : BIGINT → ASIGNATURA.id_asignatura  (ON DELETE CASCADE)
    — Material genérico de la asignatura  


── Relaciones principales ──  
ALUMNO 1 ←— (*) INSCRIPCION (*) —→ 1 GRUPO  
GRUPO  * ←— (1) ASIGNATURA  
GRUPO  1 ←— (*) SESION  
ASIGNATURA 1 ←— (*) MATERIAL 
