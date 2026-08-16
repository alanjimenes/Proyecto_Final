package logico;

import java.io.Serializable;

public class TipoAnalisis implements Serializable {
    private int codigoTipo;
    private String nombre;
    private String descripcion;

    public TipoAnalisis(int codigoTipo, String nombre, String descripcion) {
        this.codigoTipo = codigoTipo;
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public int getCodigoTipo() { return codigoTipo; }
    public void setCodigoTipo(int codigoTipo) { this.codigoTipo = codigoTipo; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
}