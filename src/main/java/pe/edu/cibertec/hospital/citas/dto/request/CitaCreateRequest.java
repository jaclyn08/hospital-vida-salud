package pe.edu.cibertec.hospital.citas.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class CitaCreateRequest {

    @NotBlank(message = "El codigo de cita es obligatorio")
    private String codigoCita;

    @NotNull(message = "El ID del paciente es obligatorio")
    private Long pacienteId;

    @NotBlank(message = "La especialidad es obligatoria")
    private String especialidad;

    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    @Future(message = "La fecha y hora de la cita debe ser futura")
    private LocalDateTime fechaHora;

    @NotBlank(message = "El codigo del profesional es obligatorio")
    private String profesionalCodigo;

    private String motivo;

    public CitaCreateRequest() {
    }

    public CitaCreateRequest(String codigoCita, Long pacienteId, String especialidad, LocalDateTime fechaHora, String profesionalCodigo, String motivo) {
        this.codigoCita = codigoCita;
        this.pacienteId = pacienteId;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.profesionalCodigo = profesionalCodigo;
        this.motivo = motivo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String codigoCita;
        private Long pacienteId;
        private String especialidad;
        private LocalDateTime fechaHora;
        private String profesionalCodigo;
        private String motivo;

        public Builder codigoCita(String codigoCita) { this.codigoCita = codigoCita; return this; }
        public Builder pacienteId(Long pacienteId) { this.pacienteId = pacienteId; return this; }
        public Builder especialidad(String especialidad) { this.especialidad = especialidad; return this; }
        public Builder fechaHora(LocalDateTime fechaHora) { this.fechaHora = fechaHora; return this; }
        public Builder profesionalCodigo(String profesionalCodigo) { this.profesionalCodigo = profesionalCodigo; return this; }
        public Builder motivo(String motivo) { this.motivo = motivo; return this; }

        public CitaCreateRequest build() {
            return new CitaCreateRequest(codigoCita, pacienteId, especialidad, fechaHora, profesionalCodigo, motivo);
        }
    }

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
}
