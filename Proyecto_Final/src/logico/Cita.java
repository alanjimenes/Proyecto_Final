package logico;

import java.time.LocalDateTime;

public class Cita {

	private String codigo_cita;
	private LocalDateTime fechaHora;
	private Cliente cliente;
	private Medico medico;
	private String estado;

	public Cita(LocalDateTime fechaHora, Cliente cliente, Medico medico, String estado) {
		super();
		this.fechaHora = fechaHora;
		this.cliente = cliente;
		this.medico = medico;
		this.estado = estado;
	}

	public String getCodigo_cita() {
		return codigo_cita;
	}

	public void setCodigo_cita(String codigo_cita) {
		this.codigo_cita = codigo_cita;
	}

	public LocalDateTime getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(LocalDateTime fechaHora) {
		this.fechaHora = fechaHora;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}


}