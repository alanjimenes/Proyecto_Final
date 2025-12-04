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
	private String genero;

	public Cliente(String cedula, String nombre, String apellido, String telefono, 
			LocalDate fechaNacimiento, 
			String direccion, boolean activo, String numExpediente, Historial historial, boolean enfermo,
			ArrayList<RegistroVacunacion> regVacunas, String genero) {


		super(cedula, nombre, apellido, telefono, fechaNacimiento, direccion, activo);

		this.numExpediente = numExpediente;
		this.historial = historial;
		this.enfermo = enfermo;
		this.regVacunas = regVacunas;
		this.genero = genero;
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

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}
}