package logico;

import java.util.ArrayList;


public class Historial {
	private String codigo_Hist;
	private Paciente paciente;
	private ArrayList<Consulta> consultas;

	public Historial(String codigo_Hist, Paciente paciente, ArrayList<Consulta> consultas) {
		super();
		this.codigo_Hist = codigo_Hist;
		this.paciente = paciente;
		this.consultas = consultas;
	}

	public String getCodigo_Hist() {
		return codigo_Hist;
	}
	public void setCodigo_Hist(String codigo_Hist) {
		this.codigo_Hist = codigo_Hist;
	}
	public Paciente getPaciente() {
		return paciente;
	}
	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}
	public ArrayList<Consulta> getConsultas() {
		return consultas;
	}
	public void setConsultas(ArrayList<Consulta> consultas) {
		this.consultas = consultas;
	}

	public ArrayList<Consulta> getConsultasVisibleMedico(Medico medico){

	}

	public String generarResumen() {

	}



}