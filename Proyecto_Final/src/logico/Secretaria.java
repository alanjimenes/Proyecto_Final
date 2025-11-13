package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Secretaria extends Persona {

	public Secretaria(String cedula, String nombre, String apellido, LocalDate fechaNacimiento, String telefono,
			String direccion, Historial historial, boolean enfermo, ArrayList<RegistroVacunacion> regVacunas) {
		super(cedula, nombre, apellido, fechaNacimiento, telefono, direccion, historial, enfermo, regVacunas);
	}


}
