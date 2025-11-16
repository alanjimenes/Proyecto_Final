package logico;

import java.util.ArrayList;

public class Historial {

	private String codigo_Hist;
	private ArrayList<Consulta> consultas;

	public Historial(String codigo_Hist) {
		super();
		this.codigo_Hist = codigo_Hist;
		this.consultas = new ArrayList<>();
	}

	public String getCodigo_Hist() {
		return codigo_Hist;
	}

	public void setCodigo_Hist(String codigo_Hist) {
		this.codigo_Hist = codigo_Hist;
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

		resumen += "================== RESUMEN DE HISTORIAL CLÍNICO ====================\n";
		resumen += "--------------------------------------------------------------------\n\n";

		int consultasEnResumen = 0;

		if (this.consultas == null) {
			resumen += "ERROR: El historial de consultas no ha sido inicializado (es nulo).\n";
		} else {
			for (Consulta cons : this.consultas) {

				if (cons != null) {

					boolean marcadaPorDoctor = cons.isAgregarAlResumen();
					boolean esVigilancia = cons.bajoVigilancia();

					if (marcadaPorDoctor || esVigilancia) {
						consultasEnResumen++;
						resumen += "--------- CONSULTA #" + consultasEnResumen + " ----------\n";

						if (esVigilancia) {
							resumen += "***** BAJO VIGILANCIA ESTRICTA *****\n";
						}

						String fechaStr = "Fecha no disponible";
						if (cons.getFechaConsulta() != null) {
							fechaStr = cons.getFechaConsulta().toString();
						}
						resumen += "Fecha: " + fechaStr + "\n";

						String doctorStr = "Doctor no especificado";
						String especStr = "Especialidad no especificada";

						if (cons.getMedico() != null) {
							doctorStr = cons.getMedico().getNombre() + " " + cons.getMedico().getApellido();

							if (cons.getMedico().getEspecialidad() != null) {
								especStr = cons.getMedico().getEspecialidad().getNombre();
							}
						}
						resumen += "Doctor: " + doctorStr + "\n";
						resumen += "Especialidad: " + especStr + "\n";

						resumen += "Síntomas: " + (cons.getSintomas() != null ? cons.getSintomas() : "") + "\n";
						resumen += "Diagnóstico: " + (cons.getDiagnostico() != null ? cons.getDiagnostico() : "")
								+ "\n";
						resumen += "\n";
					}
				}
			}
		}

		if (consultasEnResumen == 0 && this.consultas != null) {
			resumen += "No hay consultas marcadas para el resumen ni casos bajo vigilancia.\n";
		}

		resumen += "====================== FIN DEL RESUMEN ======================\n";

		return resumen;
	}

	public void agregarConsulta(Consulta nuevaConsulta) {

		if (nuevaConsulta == null) {
			return;
		}

		if (this.consultas == null) {
			this.consultas = new ArrayList<>();
		}

		this.consultas.add(nuevaConsulta);
	}

}