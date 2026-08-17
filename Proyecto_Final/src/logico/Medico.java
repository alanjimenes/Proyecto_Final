package logico;

import java.time.LocalDate;

public class Medico extends Persona {
	private int maxCitasPorDia;
	private User usuario;
	private Especialidad especialidad;

	public Medico() {
		super();
	}

	public Medico(int codigoPersona, LocalDate fechaNacimiento, String nombre, String apellido, String cedula, String telefono, boolean estado, String direccion, String genero, int maxCitasPorDia, User usuario, Especialidad especialidad) {
		super(codigoPersona, fechaNacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero);
		this.maxCitasPorDia = maxCitasPorDia;
		this.usuario = usuario;
		this.especialidad = especialidad;
	}

	public int getMaxCitasPorDia() { return maxCitasPorDia; }
	public void setMaxCitasPorDia(int maxCitasPorDia) { this.maxCitasPorDia = maxCitasPorDia; }
	public User getUsuario() { return usuario; }
	public void setUsuario(User usuario) { this.usuario = usuario; }
	public Especialidad getEspecialidad() { return especialidad; }
	public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

	@Override
	public String toString() {
		return nombre + " " + apellido + " (" + (especialidad != null ? especialidad.getNombre() : "Sin Especialidad") + ")";
	}
}