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

	public ArrayList<Consulta> getConsultasVisibleMedico(Medico medico) {
		ArrayList<Consulta> consultasVisibles = new ArrayList<>();

		for (Consulta cons : this.consultas) {
			boolean esSuConsulta = cons.getMedico().getCedula().equals(medico.getCedula());
			boolean esVigilancia = cons.bajoVigilancia();
			if (esSuConsulta || esVigilancia) {
				consultasVisibles.add(cons);
			}
		}

		return consultasVisibles;
	}

	public String generarResumen() {

		String resumen = ""; 

		resumen += "================== RESUMEN DE HISTORIAL CLiNICO ====================\n";
		resumen += "Paciente: " + this.paciente.getNombre() + " " + this.paciente.getApellido() + "\n";
		resumen += "Cédula: " + this.paciente.getCedula() + "\n";
		resumen += "--------------------------------------------------------------------\n\n";

		int consultasEnResumen = 0;

		for (Consulta cons : this.consultas) {
			boolean marcadaPorDoctor = cons.isAgregarAlResumen();
			boolean esVigilancia = cons.bajoVigilancia();
			if (marcadaPorDoctor || esVigilancia) {
				consultasEnResumen++;
				resumen += "--------- CONSULTA #" + consultasEnResumen + " ----------\n";
				if (esVigilancia) {
					resumen += "***** BAJO VIGILANCIA ESTRICTA *****\n";
				}
				resumen += "Fecha: " + cons.getFechaConsulta().toString() + "\n";
				resumen += "Doctor: " + cons.getMedico().getNombre() + " " + cons.getMedico().getApellido() + "\n";
				resumen += "Especialidad: " + cons.getMedico().getEspecialidad().getNombre() + "\n";
				resumen += "Síntomas: " + cons.getSintomas() + "\n";
				resumen += "Diagnóstico: " + cons.getDiagnostico() + "\n";
				resumen += "\n";
			}
		}

		if (consultasEnResumen == 0) {
			resumen += "No hay consultas marcadas para el resumen ni casos bajo vigilancia.\n";
		}

		resumen += "====================== FIN DEL RESUMEN ======================\n";

		return resumen;
	}

}