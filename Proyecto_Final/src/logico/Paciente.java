package logico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona {

	private String numExpediente;

	private HistoriaClinica historiaClinica;
	private Vivienda vivienda;
	private List<Cita> citas;
	private List<RegistroVacunacion> regVacunas;

	public Paciente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono);
		this.numExpediente = "PAC-" + cedula;

		this.historiaClinica = new HistoriaClinica(this);
		this.citas = new ArrayList<>();
		this.regVacunas = new ArrayList<>();
	}

	
	public void getConsulta(Consulta newConsulta) {
		this.historiaClinica.agregarConsulta(newConsulta);
	}

	public List<Consulta> getConsultasPasadas() {
		return this.historiaClinica.getConsultas();
	}

	public void regVacuna(Vacuna vacuna, LocalDate fecha) {
		RegistroVacunacion registro = new RegistroVacunacion(this, vacuna, fecha, true);
		this.regVacunas.add(registro);
	}


	public String getNumExpediente() {
		return numExpediente;
	}


	public void setNumExpediente(String numExpediente) {
		this.numExpediente = numExpediente;
	}


	public HistoriaClinica getHistoriaClinica() {
		return historiaClinica;
	}


	public void setHistoriaClinica(HistoriaClinica historiaClinica) {
		this.historiaClinica = historiaClinica;
	}


	public Vivienda getVivienda() {
		return vivienda;
	}


	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}


	public List<Cita> getCitas() {
		return citas;
	}


	public void setCitas(List<Cita> citas) {
		this.citas = citas;
	}


	public List<RegistroVacunacion> getRegVacunas() {
		return regVacunas;
	}


	public void setRegVacunas(List<RegistroVacunacion> regVacunas) {
		this.regVacunas = regVacunas;
	}
}