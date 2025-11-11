package logico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Paciente extends Persona {

	private String numeroExpediente;

	private HistoriaClinica historiaClinica;
	private Vivienda vivienda;
	private List<Cita> citasProgramadas;
	private List<RegistroVacunacion> registroVacunas;

	public Paciente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono);
		this.numeroExpediente = "PAC-" + cedula;

		this.historiaClinica = new HistoriaClinica(this);
		this.citasProgramadas = new ArrayList<>();
		this.registroVacunas = new ArrayList<>();
	}

	public HistoriaClinica getHistoriaClinica() {
		return historiaClinica;
	}

	public Vivienda getVivienda() {
		return vivienda;
	}

	public void setVivienda(Vivienda vivienda) {
		this.vivienda = vivienda;
	}

	public List<Cita> getCitasProgramadas() {
		return citasProgramadas;
	}

	public List<RegistroVacunacion> getRegistroVacunas() {
		return registroVacunas;
	}

	public String getNumeroExpediente() {
		return numeroExpediente;
	}

	public void agregarCita(Cita nuevaCita) {
		this.citasProgramadas.add(nuevaCita);
	}

	public void cancelarCita(Cita citaACancelar) {
		if (citaACancelar.getFechaHora().toLocalDate().isAfter(LocalDate.now().minusDays(1))) {
			this.citasProgramadas.remove(citaACancelar);
		}
	}

	public void agregarConsultaAlHistorial(Consulta nuevaConsulta) {
		this.historiaClinica.agregarConsulta(nuevaConsulta);
	}

	public List<Consulta> getConsultasPasadas() {
		return this.historiaClinica.getConsultas();
	}

	public void registrarVacunaAplicada(Vacuna vacuna, LocalDate fecha) {
		RegistroVacunacion registro = new RegistroVacunacion(this, vacuna, fecha, true);
		this.registroVacunas.add(registro);
	}
}