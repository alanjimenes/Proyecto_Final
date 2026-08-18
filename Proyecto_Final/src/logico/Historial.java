package logico;

public class Historial {
    private int codigoHistorial;
    private Cliente cliente;

    public Historial() {
    }

    public Historial(int codigoHistorial, Cliente cliente) {
        this.codigoHistorial = codigoHistorial;
        this.cliente = cliente;
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
}