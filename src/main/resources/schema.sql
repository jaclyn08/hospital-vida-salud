CREATE TABLE IF NOT EXISTS citas (
    id BIGSERIAL PRIMARY KEY,
    codigo_cita VARCHAR(50) NOT NULL UNIQUE,
    paciente_id BIGINT NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    fecha_hora TIMESTAMP NOT NULL,
    profesional_codigo VARCHAR(50) NOT NULL,
    motivo VARCHAR(255),
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS auditoria_eventos (
    id BIGSERIAL PRIMARY KEY,
    tipo_evento VARCHAR(50) NOT NULL,
    origen_servicio VARCHAR(50) NOT NULL,
    payload_json TEXT NOT NULL,
    fecha_registro TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de Pacientes (para persistencia reactiva R2DBC y validaciones sincronas)
CREATE TABLE IF NOT EXISTS pacientes (
    id BIGSERIAL PRIMARY KEY,
    tipo_documento VARCHAR(20) NOT NULL,
    numero_documento VARCHAR(20) NOT NULL UNIQUE,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVO',
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_citas_paciente ON citas(paciente_id);
CREATE INDEX IF NOT EXISTS idx_citas_profesional_fecha ON citas(profesional_codigo, fecha_hora);

-- Registro inicial de Paciente de prueba
INSERT INTO pacientes (tipo_documento, numero_documento, nombres, apellidos, estado)
VALUES ('DNI', '12345678', 'Carlos', 'Mendoza', 'ACTIVO');

INSERT INTO pacientes (tipo_documento, numero_documento, nombres, apellidos, estado)
VALUES ('DNI', '87654321', 'Ana', 'Gomez', 'ACTIVO');

-- Registro inicial de prueba para asegurar existencia de Cita ID 1 en estado PENDIENTE
INSERT INTO citas (codigo_cita, paciente_id, especialidad, fecha_hora, profesional_codigo, motivo, estado)
VALUES ('CIT-1001', 1, 'Cardiología', TIMESTAMP '2026-11-20 10:00:00', 'MED-001', 'Consulta inicial de prueba', 'PENDIENTE');

-- Registros iniciales de Auditoria de Eventos asincronos
INSERT INTO auditoria_eventos (tipo_evento, origen_servicio, payload_json)
VALUES ('CITA_REGISTRADA', 'citas-service', '{"eventoId":"evt-901","tipoEvento":"CITA_REGISTRADA","citaId":1,"codigoCita":"CIT-1001","pacienteId":1,"especialidad":"Cardiologia","estado":"PENDIENTE"}');

INSERT INTO auditoria_eventos (tipo_evento, origen_servicio, payload_json)
VALUES ('CITA_CONFIRMADA', 'citas-service', '{"eventoId":"evt-902","tipoEvento":"CITA_CONFIRMADA","citaId":1,"codigoCita":"CIT-1001","pacienteId":1,"especialidad":"Cardiologia","estado":"CONFIRMADA"}');

