package logico;

import java.time.LocalDate;
import java.util.ArrayList;


public class Paciente extends Persona {

	private String numExpediente;
	private Historial historial;
	private ArrayList<Cita> citas;
	private ArrayList<RegistroVacunacion> regVacunas;

	public Paciente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String numExpediente, Historial historial, ArrayList<Cita> citas, ArrayList<RegistroVacunacion> regVacunas) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono);
		this.numExpediente = numExpediente;
		this.setHistorial(historial);
		this.citas = citas;
		this.regVacunas = regVacunas;
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


	public ArrayList<RegistroVacunacion> getRegVacunas() {
		return regVacunas;
	}


	public void setRegVacunas(ArrayList<RegistroVacunacion> regVacunas) {
		this.regVacunas = regVacunas;
	}

	public Historial getHistorial() {
		return historial;
	}


	public void setHistorial(Historial historial) {
		this.historial = historial;
	}

	public void regVacuna(Vacuna vacuna, LocalDate fecha) {
		RegistroVacunacion registro = new RegistroVacunacion(this, vacuna, fecha, true);
		this.regVacunas.add(registro);
	}


}