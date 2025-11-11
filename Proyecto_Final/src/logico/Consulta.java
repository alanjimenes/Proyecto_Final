package logico;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Consulta {

	private LocalDate fechaConsulta;
	private String sintomas;
	private String diagnostico;
	private boolean importanteParaResumen;

	private Medico medico;
	private Paciente paciente;
	private List<Enfermedad> enfermedadesDiagnosticadas;

	public Consulta(LocalDate fechaConsulta, String sintomas, String diagnostico, Medico medico, Paciente paciente) {
		this.fechaConsulta = fechaConsulta;
		this.sintomas = sintomas;
		this.diagnostico = diagnostico;
		this.medico = medico;
		this.paciente = paciente;
		this.enfermedadesDiagnosticadas = new ArrayList<>();
		this.importanteParaResumen = false;
	}

	public void agregarEnfermedad(Enfermedad enfermedad) {
		this.enfermedadesDiagnosticadas.add(enfermedad);
	}

	public boolean tieneEnfermedadBajoVigilancia() {
		for (Enfermedad e : enfermedadesDiagnosticadas) {
			if (e.isBajoVigilancia()) {
				return true;
			}
		}
		return false;
	}

	public LocalDate getFechaConsulta() {
		return fechaConsulta;
	}

	public String getSintomas() {
		return sintomas;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public Medico getMedico() {
		return medico;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public List<Enfermedad> getEnfermedadesDiagnosticadas() {
		return enfermedadesDiagnosticadas;
	}

	public boolean esImportanteParaResumen() {
		return importanteParaResumen;
	}

	public void setImportanteParaResumen(boolean importante) {
		this.importanteParaResumen = importante;
	}
}