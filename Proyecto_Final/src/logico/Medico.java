package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Medico extends Persona {

	private Especialidad especialidad;
	private int maxCitasPorDia;
	private ArrayList<Consulta> consultasRealizadas;

	public Medico(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			Especialidad especialidad) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono);
		this.especialidad = especialidad;
		this.maxCitasPorDia = 10; 
		this.consultasRealizadas = new ArrayList<>();
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
		return consultasRealizadas;
	}

	public void agregarConsultaRealizada(Consulta consulta) {
		this.consultasRealizadas.add(consulta);
	}
}