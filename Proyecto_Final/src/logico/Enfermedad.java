package logico;

public class Enfermedad {

	private String nombre;
	private String descripcion;
	private boolean bajoVigilancia;

	public Enfermedad(String nombre, String descripcion, boolean bajoVigilancia) {
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.bajoVigilancia = bajoVigilancia;
	}

	public String getNombre() {
		return nombre;
	}

	public boolean isBajoVigilancia() {
		return bajoVigilancia;
	}

	public void setBajoVigilancia(boolean bajoVigilancia) {
		this.bajoVigilancia = bajoVigilancia;
	}
}