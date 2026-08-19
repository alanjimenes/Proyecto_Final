package logico;

public class Medicamento implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int codigoMedicamento;
    private String nombre;
    private String concentracion;
    private String descripcion;

    public Medicamento() {
    }

    public Medicamento(int codigoMedicamento, String nombre, String concentracion, String descripcion) {
        this.codigoMedicamento = codigoMedicamento;
        this.nombre = nombre;
        this.concentracion = concentracion;
        this.descripcion = descripcion;
    }

    public int getCodigoMedicamento() {
        return codigoMedicamento;
    }

    public void setCodigoMedicamento(int codigoMedicamento) {
        this.codigoMedicamento = codigoMedicamento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getConcentracion() {
        return concentracion;
    }

    public void setConcentracion(String concentracion) {
        this.concentracion = concentracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}