package logico;

public class RecetaMedica implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int codigoRec;
    private Consulta consulta;
    private Medicamento medicamento;
    private String frecuencia;
    private String duracion;
    private String dosis;
    private String descripcion;

    public RecetaMedica() {
    }

    public RecetaMedica(int codigoRec, Consulta consulta, Medicamento medicamento, String frecuencia, String duracion, String dosis, String descripcion) {
        this.codigoRec = codigoRec;
        this.consulta = consulta;
        this.medicamento = medicamento;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
        this.dosis = dosis;
        this.descripcion = descripcion;
    }

    public int getCodigoRec() {
        return codigoRec;
    }

    public void setCodigoRec(int codigoRec) {
        this.codigoRec = codigoRec;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}