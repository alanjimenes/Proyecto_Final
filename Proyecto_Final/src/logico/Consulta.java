package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Consulta {
	private String codigo_cons;
	private LocalDate fechaConsulta;
	private String sintomas;
	private String diagnostico;


	private Medico medico;
	private Paciente paciente;
	private ArrayList<Enfermedad> enfermedadesDiag;

	public Consulta(String codigo_cons,LocalDate fechaConsulta, String sintomas, String diagnostico, Medico medico, Paciente paciente) {
		
		this.fechaConsulta = fechaConsulta;
		this.sintomas = sintomas;
		this.diagnostico = diagnostico;
		this.medico = medico;
		this.paciente = paciente;
		this.enfermedadesDiag = new ArrayList<>();
		
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


	public Paciente getPaciente() {
		return paciente;
	}


	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}


	public ArrayList<Enfermedad> getEnfermedadesDiag() {
		return enfermedadesDiag;
	}


	public void setEnfermedadesDiag(ArrayList<Enfermedad> enfermedadesDiag) {
		this.enfermedadesDiag = enfermedadesDiag;
	}

	public void agregarEnfermedad(Enfermedad enfermedad) {
		this.enfermedadesDiag.add(enfermedad);
	}

	public boolean  bajoVigilancia() {
		for (Enfermedad e : enfermedadesDiag) {
			if (e.isBajoVigilancia()) {
				return true;
			}
		}
		return false;
	}



}