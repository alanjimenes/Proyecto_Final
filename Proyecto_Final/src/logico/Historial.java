package logico;

import java.util.ArrayList;

public class Historial implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int codigoHistorial;
    private Cliente cliente;
    private ArrayList<Consulta> consultas;

    public Historial() {
        this.consultas = new ArrayList<>();
    }

    public Historial(int codigoHistorial, Cliente cliente) {
        this.codigoHistorial = codigoHistorial;
        this.cliente = cliente;
        this.consultas = new ArrayList<>();
    }

    public int getCodigoHistorial() {
        return codigoHistorial;
    }

    public void setCodigoHistorial(int codigoHistorial) {
        this.codigoHistorial = codigoHistorial;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public ArrayList<Consulta> getConsultas() {
        return consultas;
    }

    public void setConsultas(ArrayList<Consulta> consultas) {
        this.consultas = consultas;
    }
}