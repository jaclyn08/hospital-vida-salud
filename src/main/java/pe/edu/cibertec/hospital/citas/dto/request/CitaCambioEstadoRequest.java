package pe.edu.cibertec.hospital.citas.dto.request;

import jakarta.validation.constraints.NotBlank;

public class CitaCambioEstadoRequest {

    @NotBlank(message = "El nuevo estado es obligatorio")
    private String nuevoEstado;

    private String motivoCambio;

    public CitaCambioEstadoRequest() {
    }

    public CitaCambioEstadoRequest(String nuevoEstado, String motivoCambio) {
        this.nuevoEstado = nuevoEstado;
        this.motivoCambio = motivoCambio;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String nuevoEstado;
        private String motivoCambio;

        public Builder nuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; return this; }
        public Builder motivoCambio(String motivoCambio) { this.motivoCambio = motivoCambio; return this; }

        public CitaCambioEstadoRequest build() {
            return new CitaCambioEstadoRequest(nuevoEstado, motivoCambio);
        }
    }

    public String getNuevoEstado() { return nuevoEstado; }
    public void setNuevoEstado(String nuevoEstado) { this.nuevoEstado = nuevoEstado; }

    public String getMotivoCambio() { return motivoCambio; }
    public void setMotivoCambio(String motivoCambio) { this.motivoCambio = motivoCambio; }
}
