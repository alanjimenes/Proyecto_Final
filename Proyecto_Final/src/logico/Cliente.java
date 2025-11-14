package logico;

import java.time.LocalDate;
import java.util.ArrayList;


public class Cliente extends Persona { 

    private String numExpediente; 
    private Historial historial;
    private boolean enfermo;
    private ArrayList<RegistroVacunacion> regVacunas;

    public Cliente(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
            String direccion) {
        
        super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion);

        this.enfermo = false; 
        this.historial = new Historial("HIST-" + cedula); 
        this.regVacunas = new ArrayList<>(); 
        
    
    }


    public String getNumExpediente() {
        return numExpediente;
    }

    public void setNumExpediente(String numExpediente) {
        this.numExpediente = numExpediente;
    }

    public Historial getHistorial() {
        return historial;
    }

    public void setHistorial(Historial historial) {
        this.historial = historial;
    }

    public boolean isEnfermo() {
        return enfermo;
    }

    public void setEnfermo(boolean enfermo) {
        this.enfermo = enfermo;
    }

    public ArrayList<RegistroVacunacion> getRegVacunas() {
        return regVacunas;
    }

    public void setRegVacunas(ArrayList<RegistroVacunacion> regVacunas) {
        this.regVacunas = regVacunas;
    }
}