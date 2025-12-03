package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class RegistroVacunacion implements Serializable {

	private static final long serialVersionUID = 1L;
	private String codigo_reg;
	private Cliente cliente;
	private Vacuna vacuna;
	private LocalDate fecha;
	private Medico medico;
	private boolean aplicada;

	public RegistroVacunacion(Cliente cliente, Vacuna vacuna, LocalDate fecha, Medico medico, boolean aplicada) {
		super(); 
		this.cliente = cliente;
		this.vacuna = vacuna;
		this.fecha = fecha;
		this.medico = medico;
		this.aplicada = aplicada;
	}

	public String getCodigo_reg() {
		return codigo_reg;
	}

	public void setCodigo_reg(String codigo_reg) {
		this.codigo_reg = codigo_reg;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setPaciente(Cliente cliente) {
		this.cliente = cliente;
	}

	public Vacuna getVacuna() {
		return vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isAplicada() {
		return aplicada;
	}

	public void setAplicada(boolean aplicada) {
		this.aplicada = aplicada;
	}

	public Medico getMedico() {
		return medico;
	}

	public void setMedico(Medico medico) {
		this.medico = medico;
	}
}
