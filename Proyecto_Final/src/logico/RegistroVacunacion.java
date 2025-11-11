package logico;

import java.time.LocalDate;

public class RegistroVacunacion {

	private Paciente paciente;
	private Vacuna vacuna;
	private LocalDate fechaAplicacion;
	private boolean aplicada;

	public RegistroVacunacion(Paciente p, Vacuna v, LocalDate f, boolean a) {
		this.paciente = p;
		this.vacuna = v;
		this.fechaAplicacion = f;
		this.aplicada = a;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public Vacuna getVacuna() {
		return vacuna;
	}

	public LocalDate getFechaAplicacion() {
		return fechaAplicacion;
	}

	public boolean isAplicada() {
		return aplicada;
	}

	public void setAplicada(boolean aplicada) {
		this.aplicada = aplicada;
	}
}
