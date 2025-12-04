package logico;

import java.io.Serializable;

public class Vacuna implements Serializable {
	private static final long serialVersionUID = 1L;
	private String codigo_vacun;
	private String nombre;
	private String descripcion;
	private boolean activo;

	public Vacuna(String codigo_vacun, String nombre, String descripcion) {
		super();
		this.codigo_vacun = codigo_vacun;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.activo = true;
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

	public boolean isActivo() { 
		return activo;
	}

	public void setActivo(boolean activo) { 
		this.activo = activo; 
	}

	@Override
	public String toString() {
		return nombre;
	}
}