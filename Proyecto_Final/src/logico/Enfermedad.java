package logico;

public class Enfermedad implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int codigoEnfermedad;
    private boolean activo;
    private String nombre;
    private boolean vigilancia;
    private String descripcion;

    public Enfermedad() {
    }

    public Enfermedad(int codigoEnfermedad, boolean activo, String nombre, boolean vigilancia, String descripcion) {
        this.codigoEnfermedad = codigoEnfermedad;
        this.activo = activo;
        this.nombre = nombre;
        this.vigilancia = vigilancia;
        this.descripcion = descripcion;
    }

    public int getCodigoEnfermedad() {
        return codigoEnfermedad;
    }

    public void setCodigoEnfermedad(int codigoEnfermedad) {
        this.codigoEnfermedad = codigoEnfermedad;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isVigilancia() {
        return vigilancia;
    }

    public void setVigilancia(boolean vigilancia) {
        this.vigilancia = vigilancia;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}