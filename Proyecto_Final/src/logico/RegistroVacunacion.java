package logico;

import java.time.LocalDateTime;

public class RegistroVacunacion implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int codigoReg;
	private Cliente cliente;
	private LoteVacuna lote;
	private Enfermera enfermera;
	private LocalDateTime fecha;
	private boolean aplicada;

	public RegistroVacunacion() {
	}

	public RegistroVacunacion(int codigoReg, Cliente cliente, LoteVacuna lote, Enfermera enfermera, LocalDateTime fecha, boolean aplicada) {
		this.codigoReg = codigoReg;
		this.cliente = cliente;
		this.lote = lote;
		this.enfermera = enfermera;
		this.fecha = fecha;
		this.aplicada = aplicada;
	}

	public int getCodigoReg() { return codigoReg; }
	public void setCodigoReg(int codigoReg) { this.codigoReg = codigoReg; }

	public Cliente getCliente() { return cliente; }
	public void setCliente(Cliente cliente) { this.cliente = cliente; }

	public LoteVacuna getLote() { return lote; }
	public void setLote(LoteVacuna lote) { this.lote = lote; }

	public Enfermera getEnfermera() { return enfermera; }
	public void setEnfermera(Enfermera enfermera) { this.enfermera = enfermera; }

	public LocalDateTime getFecha() { return fecha; }
	public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

	public boolean isAplicada() { return aplicada; }
	public void setAplicada(boolean aplicada) { this.aplicada = aplicada; }
}