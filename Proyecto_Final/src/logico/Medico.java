package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Medico extends Persona {
	private int maxCitasPorDia;
	private User usuario;
	private Especialidad especialidad;
	private ArrayList<Consulta> consultasRealizadas;
	private ArrayList<Cita> citasAsignadas;

	public Medico() {
		super();
		this.consultasRealizadas = new ArrayList<>();
		this.citasAsignadas = new ArrayList<>();
	}

	public Medico(int codigoPersona, LocalDate fechaNacimiento, String nombre, String apellido, String cedula, String telefono, boolean estado, String direccion, String genero, int maxCitasPorDia, User usuario, Especialidad especialidad) {
		super(codigoPersona, fechaNacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero);
		this.maxCitasPorDia = maxCitasPorDia;
		this.usuario = usuario;
		this.especialidad = especialidad;
		this.consultasRealizadas = new ArrayList<>();
		this.citasAsignadas = new ArrayList<>();
	}

	public int getMaxCitasPorDia() { return maxCitasPorDia; }
	public void setMaxCitasPorDia(int maxCitasPorDia) { this.maxCitasPorDia = maxCitasPorDia; }

	public User getUsuario() { return usuario; }
	public void setUsuario(User usuario) { this.usuario = usuario; }

	public Especialidad getEspecialidad() { return especialidad; }
	public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

	public ArrayList<Consulta> getConsultasRealizadas() { return consultasRealizadas; }
	public void setConsultasRealizadas(ArrayList<Consulta> consultasRealizadas) { this.consultasRealizadas = consultasRealizadas; }

	public ArrayList<Cita> getCitasAsignadas() { return citasAsignadas; }
	public void setCitasAsignadas(ArrayList<Cita> citasAsignadas) { this.citasAsignadas = citasAsignadas; }

	@Override
	public String toString() {
		return nombre + " " + apellido + " (" + (especialidad != null ? especialidad.getNombre() : "Sin Especialidad") + ")";
	}
}