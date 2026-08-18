package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Consulta {
	private int codigoConsulta;
	private LocalDate fechaConsulta;
	private String sintomas;
	private String diagnostico;
	private Medico medico;
	private Cliente cliente;
	private boolean bajoVigilancia;
	private EvaluacionFisica evaluacion;
	private ArrayList<RecetaMedica> recetas;
	private ArrayList<Enfermedad> enfermedadesDiag;

	public Consulta() {
		this.recetas = new ArrayList<>();
		this.enfermedadesDiag = new ArrayList<>();
	}

	public int getCodigoConsulta() { return codigoConsulta; }
	public void setCodigoConsulta(int codigoConsulta) { this.codigoConsulta = codigoConsulta; }

	public LocalDate getFechaConsulta() { return fechaConsulta; }
	public void setFechaConsulta(LocalDate fechaConsulta) { this.fechaConsulta = fechaConsulta; }

	public String getSintomas() { return sintomas; }
	public void setSintomas(String sintomas) { this.sintomas = sintomas; }

	public String getDiagnostico() { return diagnostico; }
	public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

	public Medico getMedico() { return medico; }
	public void setMedico(Medico medico) { this.medico = medico; }

	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public boolean bajoVigilancia() { return bajoVigilancia; }
	public void setBajoVigilancia(boolean bajoVigilancia) { this.bajoVigilancia = bajoVigilancia; }

	public EvaluacionFisica getEvaluacion() { return evaluacion; }
	public void setEvaluacion(EvaluacionFisica evaluacion) { this.evaluacion = evaluacion; }

	public ArrayList<RecetaMedica> getRecetas() { return recetas; }
	public void setRecetas(ArrayList<RecetaMedica> recetas) { this.recetas = recetas; }

	public ArrayList<Enfermedad> getEnfermedadesDiag() { return enfermedadesDiag; }
	public void setEnfermedadesDiag(ArrayList<Enfermedad> enfermedadesDiag) { this.enfermedadesDiag = enfermedadesDiag; }
}