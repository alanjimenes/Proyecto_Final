package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Medico extends Persona implements Serializable {

	private Especialidad especialidad;
	private int maxCitasPorDia;
	private ArrayList<Consulta> consultasRealizadas;
	private String password;

	public Medico(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, Historial historial, boolean enfermo, ArrayList<RegistroVacunacion> regVacunas,
			Especialidad especialidad, int maxCitasPorDia, ArrayList<Consulta> consultasRealizadas, String password) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion, historial, enfermo, regVacunas);
		this.especialidad = especialidad;
		this.maxCitasPorDia = maxCitasPorDia;
		this.consultasRealizadas = consultasRealizadas;
		this.password = password;
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

	public void setConsultasRealizadas(ArrayList<Consulta> consultasRealizadas) {
		this.consultasRealizadas = consultasRealizadas;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}