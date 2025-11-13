package logico;

import java.time.LocalDate;

public class RegistroVacunacion {

	private String codigo_reg;
	private Paciente paciente;
	private Vacuna vacuna;
	private transient LocalDate fecha;
	private boolean aplicada;

	public String getCodigo_reg() {
		return codigo_reg;
	}

	public void setCodigo_reg(String codigo_reg) {
		this.codigo_reg = codigo_reg;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
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

	public RegistroVacunacion(Paciente paciente, Vacuna vacuna, LocalDate fecha, boolean aplicada) {
		super();
		this.paciente = paciente;
		this.vacuna = vacuna;
		this.fecha = fecha;
		this.aplicada = aplicada;
	}


}
