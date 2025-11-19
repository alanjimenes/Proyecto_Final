package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Cliente extends Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private String numExpediente;
	private Historial historial;
	private boolean enfermo;
	private ArrayList<RegistroVacunacion> regVacunas;

	public Cliente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, boolean activo, String numExpediente, Historial historial, boolean enfermo,
			ArrayList<RegistroVacunacion> regVacunas, boolean activo2) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion, activo);
		this.numExpediente = numExpediente;
		this.historial = historial;
		this.enfermo = enfermo;
		this.regVacunas = regVacunas;
		activo = activo2;
	}

	public String getNumExpediente() {
		return numExpediente;
	}

	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}

	public Historial getHistorial() {
		return historial;
	}

	public void setHistorial(Historial historial) {
		this.historial = historial;
	}

	public boolean isEnfermo() {
		return enfermo;
	}

	public void setEnfermo(boolean enfermo) {
		this.enfermo = enfermo;
	}

	public ArrayList<RegistroVacunacion> getRegVacunas() {
		return regVacunas;
	}

	public void setRegVacunas(ArrayList<RegistroVacunacion> regVacunas) {
		this.regVacunas = regVacunas;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
}