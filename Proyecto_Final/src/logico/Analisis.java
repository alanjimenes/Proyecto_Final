package logico;

import java.time.LocalDateTime;

public class Analisis implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int codigoAnalisis;
    private Consulta consulta;
    private TipoAnalisis tipo;
    private LocalDateTime fechaOrden;
    private LocalDateTime fechaResultado;
    private String estado;
    private String resultado;

    public Analisis() {
    }

    public Analisis(int codigoAnalisis, Consulta consulta, TipoAnalisis tipo, LocalDateTime fechaOrden, LocalDateTime fechaResultado, String estado, String resultado) {
        this.codigoAnalisis = codigoAnalisis;
        this.consulta = consulta;
        this.tipo = tipo;
        this.fechaOrden = fechaOrden;
        this.fechaResultado = fechaResultado;
        this.estado = estado;
        this.resultado = resultado;
    }

    public int getCodigoAnalisis() {
        return codigoAnalisis;
    }

    public void setCodigoAnalisis(int codigoAnalisis) {
        this.codigoAnalisis = codigoAnalisis;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public TipoAnalisis getTipo() {
        return tipo;
    }

    public void setTipo(TipoAnalisis tipo) {
        this.tipo = tipo;
    }

    public LocalDateTime getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(LocalDateTime fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public LocalDateTime getFechaResultado() {
        return fechaResultado;
    }

    public void setFechaResultado(LocalDateTime fechaResultado) {
        this.fechaResultado = fechaResultado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}