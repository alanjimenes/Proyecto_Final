package logico;

import java.time.LocalDate;

public abstract class Persona {

	protected String cedula;
	protected String nombre;
	protected String apellido;
	protected LocalDate fechaNacimiento;
	protected String telefono;
	protected String direccion; 

	public Persona(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono) {
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

}