package logico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Secretaria extends Persona {

	private List<Cita> citasGestionadas;

	public Secretaria(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono);
		this.citasGestionadas = new ArrayList<>();
	}

	public Cita crearCita(Paciente paciente, Medico medico, java.time.LocalDateTime fechaHora) {
		boolean disponible = true; 
		if (disponible) {
			Cita nuevaCita = new Cita(fechaHora, paciente, medico, this);
			this.citasGestionadas.add(nuevaCita);
			paciente.agregarCita(nuevaCita);
			return nuevaCita;
		}
		return null;
	}

	public void modificarCita(Cita cita, java.time.LocalDateTime nuevaFecha) {
		cita.setFechaHora(nuevaFecha);
	}
}