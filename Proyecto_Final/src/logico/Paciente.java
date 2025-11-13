package logico;

import java.time.LocalDate;
import java.util.ArrayList;


public class Paciente extends Cliente {

	private String numExpediente;
	private ArrayList<Cita> citas;
	

	public Paciente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String numExpediente, Historial historial, ArrayList<Cita> citas, ArrayList<RegistroVacunacion> regVacunas) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono);
		this.numExpediente = numExpediente;
		this.citas = citas;
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