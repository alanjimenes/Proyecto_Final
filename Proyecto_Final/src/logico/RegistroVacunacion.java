package logico;

import java.time.LocalDate;

public class RegistroVacunacion {

	private String codigo_reg;
	private Cliente cliente;
	private Vacuna vacuna;
	private LocalDate fecha;
	private boolean aplicada;

	public RegistroVacunacion(Cliente cliente, Vacuna vacuna, LocalDate fecha, boolean aplicada) {
		super(); 
		this.cliente = cliente;
		this.vacuna = vacuna;
		this.fecha = fecha;
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
}
