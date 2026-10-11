package pe.edu.cibertec.hospital.citas.client.dto;

public class PacienteDTO {

    private Long id;
    private String tipoDocumento;
    private String numeroDocumento;
    private String nombres;
    private String apellidos;
    private String estado;

    public PacienteDTO() {
    }

    public PacienteDTO(Long id, String tipoDocumento, String numeroDocumento, String nombres, String apellidos, String estado) {
        this.id = id;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.estado = estado;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String tipoDocumento;
        private String numeroDocumento;
        private String nombres;
        private String apellidos;
        private String estado;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder tipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; return this; }
        public Builder numeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; return this; }
        public Builder nombres(String nombres) { this.nombres = nombres; return this; }
        public Builder apellidos(String apellidos) { this.apellidos = apellidos; return this; }
        public Builder estado(String estado) { this.estado = estado; return this; }

        public PacienteDTO build() {
            return new PacienteDTO(id, tipoDocumento, numeroDocumento, nombres, apellidos, estado);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTipoDocumento() { return tipoDocumento; }
    public void setTipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; }

    public String getNumeroDocumento() { return numeroDocumento; }
    public void setNumeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
