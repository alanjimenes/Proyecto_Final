package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Persona implements Serializable {
	protected int codigoPersona;
	protected LocalDate fechaNacimiento;
	protected String nombre;
	protected String apellido;
	protected String cedula;
	protected String telefono;
	protected boolean estado;
	protected String direccion;
	protected String genero;

	public Persona() {
	}

	public Persona(int codigoPersona, LocalDate fechaNacimiento, String nombre, String apellido, String cedula, String telefono, boolean estado, String direccion, String genero) {
		this.codigoPersona = codigoPersona;
		this.fechaNacimiento = fechaNacimiento;
		this.nombre = nombre;
		this.apellido = apellido;
		this.cedula = cedula;
		this.telefono = telefono;
		this.estado = estado;
		this.direccion = direccion;
		this.genero = genero;
	}

	public int getCodigoPersona() { return codigoPersona; }
	public void setCodigoPersona(int codigoPersona) { this.codigoPersona = codigoPersona; }
	public LocalDate getFechaNacimiento() { return fechaNacimiento; }
	public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }
	public String getApellido() { return apellido; }
	public void setApellido(String apellido) { this.apellido = apellido; }
	public String getCedula() { return cedula; }
	public void setCedula(String cedula) { this.cedula = cedula; }
	public String getTelefono() { return telefono; }
	public void setTelefono(String telefono) { this.telefono = telefono; }
	public boolean isEstado() { return estado; }
	public void setEstado(boolean estado) { this.estado = estado; }
	public String getDireccion() { return direccion; }
	public void setDireccion(String direccion) { this.direccion = direccion; }
	public String getGenero() { return genero; }
	public void setGenero(String genero) { this.genero = genero; }
}