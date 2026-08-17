package logico;

import java.time.LocalDate;

public class Cliente extends Persona {
	private String numExpediente;
	private boolean enfermo;
	private String antecedentes;

	public Cliente() {
		super();
	}

	public Cliente(int codigoPersona, LocalDate fechaNacimiento, String nombre, String apellido, String cedula, String telefono, boolean estado, String direccion, String genero, String numExpediente, boolean enfermo, String antecedentes) {
		super(codigoPersona, fechaNacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero);
		this.numExpediente = numExpediente;
		this.enfermo = enfermo;
		this.antecedentes = antecedentes;
	}

	public String getNumExpediente() { return numExpediente; }
	public void setNumExpediente(String numExpediente) { this.numExpediente = numExpediente; }

	public boolean isEnfermo() { return enfermo; }
	public void setEnfermo(boolean enfermo) { this.enfermo = enfermo; }

	public String getAntecedentes() { return antecedentes; }
	public void setAntecedentes(String antecedentes) { this.antecedentes = antecedentes; }

	@Override
	public String toString() {
		return nombre + " " + apellido + " (Exp: " + numExpediente + ")";
	}
}