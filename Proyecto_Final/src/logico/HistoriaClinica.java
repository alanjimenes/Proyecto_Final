package logico;

import java.util.ArrayList;
import java.util.List;

public class HistoriaClinica {
	private Paciente paciente;
	private List<Consulta> consultas;
	private String resumenAutomatico;

	public HistoriaClinica(Paciente paciente) {
		this.paciente = paciente;
		this.consultas = new ArrayList<>();
		this.resumenAutomatico = "";
	}

	public void agregarConsulta(Consulta c) {
		this.consultas.add(c);
		actualizarResumen(c);
	}

	public List<Consulta> getConsultas() {
		return this.consultas;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public String getResumenAutomatico() {
		return resumenAutomatico;
	}

	private void actualizarResumen(Consulta c) {
		if (c.esImportanteParaResumen() || c.tieneEnfermedadBajoVigilancia()) {
			this.resumenAutomatico += c.toString() + "\n";
		}
	}
}