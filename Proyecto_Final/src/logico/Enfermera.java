package logico;

import java.time.LocalDate;

public class Enfermera extends Persona {
    private User usuario;
    private String turno;

    public Enfermera() {
    }

    public Enfermera(int codigoPersona, LocalDate fechaNacimiento, String nombre, String apellido, String cedula, String telefono, boolean estado, String direccion, String genero, User usuario, String turno) {
        super(codigoPersona, fechaNacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero);
        this.usuario = usuario;
        this.turno = turno;
    }

    public User getUsuario() { return usuario; }
    public void setUsuario(User usuario) { this.usuario = usuario; }
    public String getTurno() { return turno; }
    public void setTurno(String turno) { this.turno = turno; }
}