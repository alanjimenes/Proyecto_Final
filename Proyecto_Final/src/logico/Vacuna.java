package logico;

public class Vacuna {

	private String nombre;
	private String descripcion;

	public Vacuna(String nombre, String descripcion) {
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}
}