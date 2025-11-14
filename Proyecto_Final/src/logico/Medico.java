package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Medico extends Persona implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Especialidad especialidad;
	private int maxCitasPorDia;
	private ArrayList<Consulta> consultasChecks;
	private ArrayList<Cita> citasAsignadas; 

	public Medico(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, Especialidad especialidad, int maxCitasPorDia, ArrayList<Consulta> consultasChecks, ArrayList<Cita> citasAsignadas) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion);
		this.especialidad = especialidad;
		this.maxCitasPorDia = maxCitasPorDia;
		this.consultasChecks = new ArrayList<>();
		this.citasAsignadas = new ArrayList<>();
	}

	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

	public int getMaxCitasPorDia() {
		return maxCitasPorDia;
	}


	public void setMaxCitasPorDia(int maxCitasPorDia) {
		this.maxCitasPorDia = maxCitasPorDia;
	}

	public ArrayList<Consulta> getConsultasRealizadas() {
		return consultasChecks;
	}

	public void setConsultasRealizadas(ArrayList<Consulta> consultasRealizadas) {
		this.consultasChecks = consultasRealizadas;
	}

	public void agregarConsultaRealizada(Consulta consulta) {
		this.consultasChecks.add(consulta);
	}

	public ArrayList<Cita> getCitasAsignadas() {
		return citasAsignadas;
	}

	public void setCitasAsignadas(ArrayList<Cita> citasAsignadas) {
		this.citasAsignadas = citasAsignadas;
	}

	public void agregarCitaAsignada(Cita c) {
		this.citasAsignadas.add(c);
	}



}