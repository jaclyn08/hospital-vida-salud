package pe.edu.cibertec.hospital.citas.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("citas")
public class Cita {

    @Id
    private Long id;

    @Column("codigo_cita")
    private String codigoCita;

    @Column("paciente_id")
    private Long pacienteId;

    private String especialidad;

    @Column("fecha_hora")
    private LocalDateTime fechaHora;

    @Column("profesional_codigo")
    private String profesionalCodigo;

    private String motivo;

    private String estado;

    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column("fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    public Cita() {
    }

    public Cita(Long id, String codigoCita, Long pacienteId, String especialidad, LocalDateTime fechaHora,
                String profesionalCodigo, String motivo, String estado, LocalDateTime fechaCreacion, LocalDateTime fechaActualizacion) {
        this.id = id;
        this.codigoCita = codigoCita;
        this.pacienteId = pacienteId;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.profesionalCodigo = profesionalCodigo;
        this.motivo = motivo;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaActualizacion = fechaActualizacion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String codigoCita;
        private Long pacienteId;
        private String especialidad;
        private LocalDateTime fechaHora;
        private String profesionalCodigo;
        private String motivo;
        private String estado;
        private LocalDateTime fechaCreacion;
        private LocalDateTime fechaActualizacion;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder codigoCita(String codigoCita) { this.codigoCita = codigoCita; return this; }
        public Builder pacienteId(Long pacienteId) { this.pacienteId = pacienteId; return this; }
        public Builder especialidad(String especialidad) { this.especialidad = especialidad; return this; }
        public Builder fechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; return this; }
        public Builder profesionalCodigo(String profesionalCodigo) { this.profesionalCodigo = profesionalCodigo; return this; }
        public Builder motivo(String motivo) { this.motivo = motivo; return this; }
        public Builder estado(String estado) { this.estado = estado; return this; }
        public Builder fechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; return this; }
        public Builder fechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; return this; }

        public Cita build() {
            return new Cita(id, codigoCita, pacienteId, especialidad, fechaHora, profesionalCodigo, motivo, estado, fechaCreacion, fechaActualizacion);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigoCita() { return codigoCita; }
    public void setCodigoCita(String codigoCita) { this.codigoCita = codigoCita; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public void setFechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; }

    public String getProfesionalCodigo() { return profesionalCodigo; }
    public void setProfesionalCodigo(String profesionalCodigo) { this.profesionalCodigo = profesionalCodigo; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) { this.fechaCreacion = fechaCreacion; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(LocalDateTime fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
}
