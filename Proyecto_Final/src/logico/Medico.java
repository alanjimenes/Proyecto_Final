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

	@Override
	public String toString() {
		return nombre + " " + apellido + " (" + especialidad.getNombre() + ")";
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
	public Especialidad getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(Especialidad especialidad) {
		this.especialidad = especialidad;
	}

}
