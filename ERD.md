@startuml
```plantuml
' ENTIDADES PRINCIPALES
entity "ALUMNO" as Alumno {
  * id_alumno : BIGINT
  --
    nombre      : VARCHAR(100)
    carrera     : VARCHAR(50)
    curso       : VARCHAR(20)
    email       : VARCHAR(100) UNIQUE NOT NULL
    telefono    : VARCHAR(20)
    foto        : VARCHAR(255)
}

entity "ASIGNATURA" as Asignatura {
  * id_asignatura : BIGINT
  --
    nombre        : VARCHAR(100)
    carrera       : VARCHAR(50)
}

entity "MATERIAL" as Material {
  * id_material      : BIGINT
  --
    nombre            : VARCHAR(100)
    tipo              : VARCHAR(50)    ' e.g. vídeo, pdf
    url               : VARCHAR(255)
    fecha_expiracion  : DATE           ' opcional
  --
  FK asignatura_id   : BIGINT → Asignatura.id_asignatura  (ON DELETE CASCADE)
  FK grupo_id         : BIGINT → Grupo.id_grupo         (ON DELETE CASCADE) ' opcional
}

entity "GRUPO" as Grupo {
  * id_grupo     : BIGINT
  --
    fecha_inicio : DATE
    fecha_fin    : DATE
}

entity "INSCRIPCION" as Inscripcion {
  * id_inscripcion   : BIGINT
  --
    fecha_inscripcion : DATE
  --
  FK alumno_id       : BIGINT → Alumno.id_alumno  (ON DELETE CASCADE)
  FK grupo_id        : BIGINT → Grupo.id_grupo     (ON DELETE CASCADE)
}

' SESIONES: plantilla, instancias y excepciones
entity "SESION" as Sesion {
  * id_sesion : BIGINT
  --
    dia_semana    : VARCHAR(9)   ' e.g. Lunes, Martes
    hora_inicio   : TIME
    duracion_min  : INT
    descripcion   : VARCHAR(255)
  --
  FK grupo_id    : BIGINT → Grupo.id_grupo
}

entity "SESION_INSTANCIA" as Instancia {
  * id_instancia : BIGINT
  --
    fecha_real    : DATETIME
    estado        : VARCHAR(20)  ' programada, modificada, cancelada
  --
  FK plantilla_id: BIGINT → Plantilla.id_sesion
}

entity "SESION_INCIDENCIA" as Incidencia {
  * id_incidencia  : BIGINT
  --
    motivo          : VARCHAR(255)
    tipo            : VARCHAR(20)   ' reprogramación, cancelación, sustitución
    fecha_creacion  : DATETIME
    nueva_fecha     : DATETIME [NULL]
    usuario_resp    : VARCHAR(100)
  --
  FK instancia_id  : BIGINT → Instancia.id_instancia
}

' RELACIONES
Alumno      ||--o{ Inscripcion   : "1 inscrito en n grupos"
Grupo       ||--o{ Inscripcion   : "n alumnos por grupo"
Asignatura  ||--o{ Material       : "1 asignatura tiene n materiales"
Grupo       ||--o{ Material       : "n materiales opcionales por grupo"
Grupo       ||--o{ Plantilla      : "1 grupo tiene n plantillas de sesión"
Plantilla   ||--o{ Instancia      : "genera n instancias entre fechas"
Instancia   ||--o{ Incidencia     : "puede tener n incidencias"
@enduml