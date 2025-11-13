package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Paciente extends Persona {

	public Paciente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, Historial historial, boolean enfermo, ArrayList<RegistroVacunacion> regVacunas) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion, historial, enfermo, regVacunas);
		this.historial = new Historial("H-" + cedula);
		this.citas = new ArrayList<>();

	}

	private String numExpediente;
	private ArrayList<Cita> citas;


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

}
