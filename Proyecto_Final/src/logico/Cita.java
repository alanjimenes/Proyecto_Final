package logico;

import java.time.LocalDateTime;

public class Cita implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int codigoCita;
	private LocalDateTime fechaCita;
	private Cliente cliente;
	private Medico medico;
	private String estado;
	private String motivo;

	public Cita() {
	}

	public Cita(int codigoCita, LocalDateTime fechaCita, Cliente cliente, Medico medico, String estado, String motivo) {
		this.codigoCita = codigoCita;
		this.fechaCita = fechaCita;
		this.cliente = cliente;
		this.medico = medico;
		this.estado = estado;
		this.motivo = motivo;
	}

	public int getCodigoCita() { return codigoCita; }
	public void setCodigoCita(int codigoCita) { this.codigoCita = codigoCita; }

	public LocalDateTime getFechaCita() { return fechaCita; }
	public void setFechaCita(LocalDateTime fechaCita) { this.fechaCita = fechaCita; }

	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public Medico getMedico() { return medico; }
	public void setMedico(Medico medico) { this.medico = medico; }

	public String getEstado() { return estado; }
	public void setEstado(String estado) { this.estado = estado; }

	public String getMotivo() { return motivo; }
	public void setMotivo(String motivo) { this.motivo = motivo; }
}