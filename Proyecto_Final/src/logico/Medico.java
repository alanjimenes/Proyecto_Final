package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Medico extends Persona implements Serializable {

	private static final long serialVersionUID = 1L;
	private Especialidad especialidad;
	private int maxCitasPorDia;
	private ArrayList<Consulta> consultasRealizadas;
	private ArrayList<Cita> citasAsignadas;

	public Medico(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, boolean activo, Especialidad especialidad, int maxCitasPorDia,
			ArrayList<Consulta> consultasRealizadas, ArrayList<Cita> citasAsignadas) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion, activo);
		this.especialidad = especialidad;
		this.maxCitasPorDia = maxCitasPorDia;
		this.consultasRealizadas = (consultasRealizadas != null) ? consultasRealizadas : new ArrayList<>();
		this.citasAsignadas = (citasAsignadas != null) ? citasAsignadas : new ArrayList<>();
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

	public void setConsultasRealizadas(ArrayList<Consulta> consultasRealizadas) {
		this.consultasRealizadas = consultasRealizadas;
	}

	public void agregarConsultaRealizada(Consulta consulta) {
		if (consulta == null)
			return;
		if (this.consultasRealizadas == null)
			this.consultasRealizadas = new ArrayList<>();
		if (!this.consultasRealizadas.contains(consulta)) {
			this.consultasRealizadas.add(consulta);
		}
	}

	public ArrayList<Cita> getCitasAsignadas() {
		return citasAsignadas;
	}

	public void setCitasAsignadas(ArrayList<Cita> citasAsignadas) {
		this.citasAsignadas = citasAsignadas;
	}

	public void agregarCitaAsignada(Cita c) {
		if (c == null)
			return;
		if (this.citasAsignadas == null)
			this.citasAsignadas = new ArrayList<>();
		this.citasAsignadas.add(c);
	}

	public void marcarParaResumen(Consulta consulta) {
		if (consulta == null)
			return;

		if (!this.consultasRealizadas.contains(consulta)) {
			this.consultasRealizadas.add(consulta);
		}
		consulta.setAgregarAlResumen(true);
	}

	public void diagnosticarEnfermedad(Consulta consulta, Enfermedad enfermedad) {
		if (consulta == null || enfermedad == null)
			return;
		if (consulta.getEnfermedadesDiag() == null) {
			consulta.setEnfermedadesDiag(new ArrayList<>());
		}
		if (!consulta.getEnfermedadesDiag().contains(enfermedad)) {
			consulta.getEnfermedadesDiag().add(enfermedad);
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Medico))
			return false;
		Medico medico = (Medico) o;
		return Objects.equals(getCedula(), medico.getCedula());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getCedula());
	}
}
