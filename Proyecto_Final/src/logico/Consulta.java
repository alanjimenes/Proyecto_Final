package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Consulta implements Serializable {

	private static final long serialVersionUID = 1L;
	private String codigo_cons;
	private LocalDate fechaConsulta;
	private String sintomas;
	private String diagnostico;
	private Medico medico;
	private Cliente cliente;
	private ArrayList<Enfermedad> enfermedadesDiag;
	private boolean AddResumen;

	private String antecedentes;
	private String presionArterial;
	private int frecuenciaCardiaca;
	private float temperatura;
	private float peso;
	private float talla;
	private String recetaMedica;

	public Consulta(String codigo_cons,LocalDate fechaConsulta, String sintomas, String diagnostico, Medico medico, Cliente cliente) {

		this.fechaConsulta = fechaConsulta;
		this.sintomas = sintomas;
		this.diagnostico = diagnostico;
		this.medico = medico;
		this.cliente = cliente;
		this.enfermedadesDiag = new ArrayList<>();
		this.AddResumen = false;
		this.antecedentes = "";
		this.presionArterial = "";
		this.recetaMedica = "";

	}

	public String getCodigo_cons() {
		return codigo_cons;
	}

	public void setCodigo_cons(String codigo_cons) {
		this.codigo_cons = codigo_cons;
	}

	public LocalDate getFechaConsulta() {
		return fechaConsulta;
	}

	public void setFechaConsulta(LocalDate fechaConsulta) {
		this.fechaConsulta = fechaConsulta;
	}

	public String getSintomas() {
		return sintomas;
	}

	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setPaciente(Cliente cliente) {
		this.cliente = cliente;
	}

	public ArrayList<Enfermedad> getEnfermedadesDiag() {
		return enfermedadesDiag;
	}

	public void setEnfermedadesDiag(ArrayList<Enfermedad> enfermedadesDiag) {
		this.enfermedadesDiag = enfermedadesDiag;
	}

	public boolean isAgregarAlResumen() {
		return AddResumen;
	}

	public String getAntecedentes() {
		return antecedentes;
	}

	public void setAntecedentes(String antecedentes) {
		this.antecedentes = antecedentes;
	}

	public String getPresionArterial() {
		return presionArterial;
	}

	public void setPresionArterial(String presionArterial) {
		this.presionArterial = presionArterial;
	}

	public int getFrecuenciaCardiaca() {
		return frecuenciaCardiaca;
	}

	public void setFrecuenciaCardiaca(int frecuenciaCardiaca) {
		this.frecuenciaCardiaca = frecuenciaCardiaca;
	}

	public float getTemperatura() {
		return temperatura;
	}

	public void setTemperatura(float temperatura) {
		this.temperatura = temperatura;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getTalla() {
		return talla;
	}

	public void setTalla(float talla) {
		this.talla = talla;
	}

	public String getRecetaMedica() {
		return recetaMedica;
	}

	public void setRecetaMedica(String recetaMedica) {
		this.recetaMedica = recetaMedica;
	}

	public void setAgregarAlResumen(boolean agregarAlResumen) {
		this.AddResumen = agregarAlResumen;
	}

	public void agregarEnfermedad(Enfermedad enfermedad) {
		this.enfermedadesDiag.add(enfermedad);
	}

	public boolean bajoVigilancia() {
		for (Enfermedad e : enfermedadesDiag) {
			if (e.isVigilancia()) {
				return true;
			}
		}
		return false;
	}
}