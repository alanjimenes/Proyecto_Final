package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Persona {

	protected String cedula;
	protected String nombre;
	protected String apellido;
	protected LocalDate fechaNacimiento;
	protected String telefono;
	protected String direccion;

	protected Historial historial;
	protected boolean enfermo;
	protected ArrayList<RegistroVacunacion> regVacunas;

	public Persona(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, Historial historial, boolean enfermo, ArrayList<RegistroVacunacion> regVacunas) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
		this.direccion = direccion;
		this.historial = historial;
		this.enfermo = enfermo;
		this.regVacunas = regVacunas;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
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


}