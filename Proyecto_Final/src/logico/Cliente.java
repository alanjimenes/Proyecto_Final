package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public abstract class Cliente {

	protected String cedula;
	protected String nombre;
	protected String apellido;
	protected LocalDate fechaNacimiento;
	protected String telefono;
	protected String direccion; 
	private Historial historial;
	protected boolean enfermo;
	private ArrayList<RegistroVacunacion> regVacunas;
	
	public Cliente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.fechaNacimiento = fechaNacimiento;
		this.telefono = telefono;
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


	public ArrayList<RegistroVacunacion> getRegVacunas() {
		return regVacunas;
	}


	public void setRegVacunas(ArrayList<RegistroVacunacion> regVacunas) {
		this.regVacunas = regVacunas;
	}

	public Historial getHistorial() {
		return historial;
	}


	public void setHistorial(Historial historial) {
		this.historial = historial;
	}

	public void regVacuna(Vacuna vacuna, LocalDate fecha) {
		RegistroVacunacion registro = new RegistroVacunacion(this, vacuna, fecha, true);
		this.regVacunas.add(registro);
	}


}