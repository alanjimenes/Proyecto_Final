package logico;

import java.util.ArrayList;
import java.util.List;

public class Historial {
	private Paciente paciente;
	private List<Consulta> consultas;

	public Historial(Paciente paciente) {
		this.paciente = paciente;
		this.consultas = new ArrayList<>();
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public List<Consulta> getConsultas() {
		return consultas;
	}

	public void setConsultas(List<Consulta> consultas) {
		this.consultas = consultas;
	}
	
	
}