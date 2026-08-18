package logico;

import java.time.LocalDate;

public class LoteVacuna implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private int codigoLote;
    private Vacuna vacuna;
    private String noLote;
    private LocalDate fechaVencimiento;
    private int cantidad;

    public LoteVacuna() {
    }

    public LoteVacuna(int codigoLote, Vacuna vacuna, String noLote, LocalDate fechaVencimiento, int cantidad) {
        this.codigoLote = codigoLote;
        this.vacuna = vacuna;
        this.noLote = noLote;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidad = cantidad;
    }

    public int getCodigoLote() {
        return codigoLote;
    }

    public void setCodigoLote(int codigoLote) {
        this.codigoLote = codigoLote;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public void setVacuna(Vacuna vacuna) {
        this.vacuna = vacuna;
    }

    public String getNoLote() {
        return noLote;
    }

    public void setNoLote(String noLote) {
        this.noLote = noLote;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


}