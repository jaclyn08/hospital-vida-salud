package pe.edu.cibertec.hospital.citas.dto.response;

import java.time.LocalDateTime;

public class CitaResponseDTO {

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
    private String mensaje;

    public CitaResponseDTO() {
    }

    public CitaResponseDTO(Long id, String codigoCita, Long pacienteId, String especialidad, LocalDateTime fechaHora,
                           String profesionalCodigo, String motivo, String estado, LocalDateTime fechaCreacion,
                           LocalDateTime fechaActualizacion, String mensaje) {
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
        this.mensaje = mensaje;
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
        private String mensaje;

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
        public Builder mensaje(String mensaje) { this.mensaje = mensaje; return this; }

        public CitaResponseDTO build() {
            return new CitaResponseDTO(id, codigoCita, pacienteId, especialidad, fechaHora, profesionalCodigo, motivo, estado, fechaCreacion, fechaActualizacion, mensaje);
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

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
}
