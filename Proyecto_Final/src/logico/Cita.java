package logico;

import java.time.LocalDateTime;

public class Cita {

	private LocalDateTime fechaHora;
	private Paciente paciente;
	private Medico medico;
	private Secretaria secretariaQueRegistra;
	private String estado;

	public Cita(LocalDateTime fechaHora, Paciente paciente, Medico medico, Secretaria secretaria) {
		this.fechaHora = fechaHora;
		this.paciente = paciente;
		this.medico = medico;
		this.secretariaQueRegistra = secretaria;
		this.estado = "Programada";
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

	public Medico getMedico() {
		return medico;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}