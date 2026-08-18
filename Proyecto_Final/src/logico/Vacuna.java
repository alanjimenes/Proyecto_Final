package logico;

public class Vacuna {
    private int codigoVacuna;
    private String nombre;
    private String descripcion;
    private boolean activo;

    public Vacuna() {
    }

    public Vacuna(int codigoVacuna, String nombre, String descripcion, boolean activo) {
        this.codigoVacuna = codigoVacuna;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.activo = activo;
    }

    public int getCodigoVacuna() {
        return codigoVacuna;
    }

    public void setCodigoVacuna(int codigoVacuna) {
        this.codigoVacuna = codigoVacuna;
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

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
}