package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Paciente extends Cliente {

	private String numExpediente;
	private ArrayList<Cita> citas;

	public Paciente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, Historial historial, boolean enfermo, ArrayList<RegistroVacunacion> regVacunas, String numExpediente, ArrayList<Cita> citas) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion, historial, enfermo, regVacunas);

	}

	public String getNumExpediente() {
		return numExpediente;
	}

	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public void setCitas(ArrayList<Cita> citas) {
		this.citas = citas;
	}

	public void agregarCita(Cita c) {
		this.citas.add(c);
	}

}
