package logico;

public class TipoAnalisis implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int codigoTipo;
    private String nombre;
    private String descripcion;

    public TipoAnalisis() {
    }

    public TipoAnalisis(int codigoTipo, String nombre, String descripcion) {
        this.codigoTipo = codigoTipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getCodigoTipo() {
        return codigoTipo;
    }

    public void setCodigoTipo(int codigoTipo) {
        this.codigoTipo = codigoTipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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