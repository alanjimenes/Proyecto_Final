package logico;

import java.io.Serializable;

public class Vacuna implements Serializable {
	private static final long serialVersionUID = 1L;
	private String codigo_vacun;
	private String nombre;
	private String descripcion;

	public Vacuna(String codigo_vacun, String nombre, String descripcion) {
		super();
		this.codigo_vacun = codigo_vacun;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public String getCodigo_vacun() {
		return codigo_vacun;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return nombre; // VITAL PARA LA INTERFAZ
	}
}