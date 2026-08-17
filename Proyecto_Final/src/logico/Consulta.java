package logico;

import java.time.LocalDateTime;

public class Consulta {
	private int codigoCons;
	private Medico medico;
	private Cliente cliente;
	private LocalDateTime fechaConsulta;
	private String sintomas;
	private String diagnostico;
	private boolean addResumen;

	public Consulta() {
	}

	public Consulta(int codigoCons, Medico medico, Cliente cliente, LocalDateTime fechaConsulta, String sintomas, String diagnostico, boolean addResumen) {
		this.codigoCons = codigoCons;
		this.medico = medico;
		this.cliente = cliente;
		this.fechaConsulta = fechaConsulta;
		this.sintomas = sintomas;
		this.diagnostico = diagnostico;
		this.addResumen = addResumen;
	}

	public int getCodigoCons() { return codigoCons; }
	public void setCodigoCons(int codigoCons) { this.codigoCons = codigoCons; }

	public Medico getMedico() { return medico; }
	public void setMedico(Medico medico) { this.medico = medico; }

	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public LocalDateTime getFechaConsulta() { return fechaConsulta; }
	public void setFechaConsulta(LocalDateTime fechaConsulta) { this.fechaConsulta = fechaConsulta; }

	public String getSintomas() { return sintomas; }
	public void setSintomas(String sintomas) { this.sintomas = sintomas; }

	public String getDiagnostico() { return diagnostico; }
	public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

	public boolean isAddResumen() { return addResumen; }
	public void setAddResumen(boolean addResumen) { this.addResumen = addResumen; }
}