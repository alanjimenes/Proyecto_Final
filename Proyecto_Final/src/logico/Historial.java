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

		resumen += "================== RESUMEN DE HISTORIAL CLiNICO ====================\n";

		resumen += "--------------------------------------------------------------------\n\n";



		resumen += "====================== FIN DEL RESUMEN ======================\n";

		return resumen;
	}

}