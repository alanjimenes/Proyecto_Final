package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Paciente extends Cliente {

	protected String numExpediente;
	protected ArrayList<Cita> citas;

	public Paciente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion);
		// TODO Auto-generated constructor stub
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

}
