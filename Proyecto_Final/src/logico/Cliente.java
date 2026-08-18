package logico;

import java.time.LocalDate;

public class Cliente extends Persona {
    private String numExpediente;
    private boolean enfermo;
    private String antecedentes;
    private Historial historial;

    public Cliente() {
        super();
        this.historial = new Historial();
    }

    public Cliente(int codigoPersona, LocalDate fechaNacimiento, String nombre, String apellido,
                   String cedula, String telefono, boolean estado, String direccion, String genero,
                   String numExpediente, boolean enfermo, String antecedentes, Historial historial) {
        super(codigoPersona, fechaNacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero);
        this.numExpediente = numExpediente;
        this.enfermo = enfermo;
        this.antecedentes = antecedentes;
        this.historial = historial;
    }

    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public boolean isEnfermo() {
        return enfermo;
    }

    public void setEnfermo(boolean enfermo) {
        this.enfermo = enfermo;
    }

    public String getAntecedentes() {
        return antecedentes;
    }

    public void setAntecedentes(String antecedentes) {
        this.antecedentes = antecedentes;
    }

    public Historial getHistorial() {
        return historial;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    public boolean getEstado() {
        return estado;
    }

    @Override
    public String toString() {
        return nombre + " " + apellido + " (Exp: " + numExpediente + ")";
    }
}