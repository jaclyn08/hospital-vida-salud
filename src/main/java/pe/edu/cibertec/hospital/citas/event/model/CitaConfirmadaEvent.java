package pe.edu.cibertec.hospital.citas.event.model;

import java.io.Serializable;

public class CitaConfirmadaEvent implements Serializable {

    private String eventoId;
    private String tipoEvento;
    private Long citaId;
    private String codigoCita;
    private Long pacienteId;
    private String especialidad;
    private String fechaHora;
    private String estado;
    private String timestamp;

    public CitaConfirmadaEvent() {
    }

    public CitaConfirmadaEvent(String eventoId, String tipoEvento, Long citaId, String codigoCita, Long pacienteId,
                               String especialidad, String fechaHora, String estado, String timestamp) {
        this.eventoId = eventoId;
        this.tipoEvento = tipoEvento;
        this.citaId = citaId;
        this.codigoCita = codigoCita;
        this.pacienteId = pacienteId;
        this.especialidad = especialidad;
        this.fechaHora = fechaHora;
        this.estado = estado;
        this.timestamp = timestamp;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String eventoId;
        private String tipoEvento = "CITA_CONFIRMADA";
        private Long citaId;
        private String codigoCita;
        private Long pacienteId;
        private String especialidad;
        private String fechaHora;
        private String estado = "CONFIRMADA";
        private String timestamp;

        public Builder eventoId(String eventoId) { this.eventoId = eventoId; return this; }
        public Builder tipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; return this; }
        public Builder citaId(Long citaId) { this.citaId = citaId; return this; }
        public Builder codigoCita(String codigoCita) { this.codigoCita = codigoCita; return this; }
        public Builder pacienteId(Long pacienteId) { this.pacienteId = pacienteId; return this; }
        public Builder especialidad(String especialidad) { this.especialidad = especialidad; return this; }
        public Builder fechaHora(String fechaHora) { this.fechaHora = fechaHora; return this; }
        public Builder estado(String estado) { this.estado = estado; return this; }
        public Builder timestamp(String timestamp) { this.timestamp = timestamp; return this; }

        public CitaConfirmadaEvent build() {
            return new CitaConfirmadaEvent(eventoId, tipoEvento, citaId, codigoCita, pacienteId, especialidad, fechaHora, estado, timestamp);
        }
    }

    public String getEventoId() { return eventoId; }
    public void setEventoId(String eventoId) { this.eventoId = eventoId; }

    public String getTipoEvento() { return tipoEvento; }
    public void setTipoEvento(String tipoEvento) { this.tipoEvento = tipoEvento; }

    public Long getCitaId() { return citaId; }
    public void setCitaId(Long citaId) { this.citaId = citaId; }

    public String getCodigoCita() { return codigoCita; }
    public void setCodigoCita(String codigoCita) { this.codigoCita = codigoCita; }

    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getFechaHora() { return fechaHora; }
    public void setFechaHora(String fechaHora) { this.fechaHora = fechaHora; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
