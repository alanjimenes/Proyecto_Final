package logico;

public class Especialidad {
	private int codigoEspecialidad;
	private String nombre;

	public Especialidad() {
	}

	public Especialidad(int codigoEspecialidad, String nombre) {
		this.codigoEspecialidad = codigoEspecialidad;
		this.nombre = nombre;
	}

	public int getCodigoEspecialidad() { return codigoEspecialidad; }
	public void setCodigoEspecialidad(int codigoEspecialidad) { this.codigoEspecialidad = codigoEspecialidad; }

	public String getNombre() { return nombre; }
	public void setNombre(String nombre) { this.nombre = nombre; }

	@Override
	public String toString() {
		return nombre;
	}
}