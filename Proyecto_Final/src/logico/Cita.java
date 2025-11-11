package logico;

import java.time.LocalDateTime;

public class Cita {

	private String codigo_cita;
	private LocalDateTime fechaHora;
	private Paciente paciente;
	private Medico medico;
	private String estado;

	public Cita(String codigo_cita, LocalDateTime fechaHora, Paciente paciente, Medico medico, String estado) {
		super();
		this.codigo_cita = codigo_cita;
		this.fechaHora = fechaHora;
		this.paciente = paciente;
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

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
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