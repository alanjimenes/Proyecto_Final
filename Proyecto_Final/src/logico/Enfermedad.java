package logico;

import java.io.Serializable;
import java.util.Objects;

public class Enfermedad implements Serializable {
	private static final long serialVersionUID = 1L;
	private String codigo_sick;
	private String nombre;
	private String descripcion;
	private boolean vigilancia;

	public Enfermedad(String codigo_sick, String nombre, String descripcion, boolean vigilancia) {
		super();
		this.codigo_sick = codigo_sick;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.vigilancia = vigilancia;
	}

	public String getCodigo_sick() {
		return codigo_sick;
	}

	public void setCodigo_sick(String codigo_sick) {
		this.codigo_sick = codigo_sick;
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

	public boolean isVigilancia() {
		return vigilancia;
	}

	public void setVigilancia(boolean vigilancia) {
		this.vigilancia = vigilancia;
	}

	@Override
	public String toString() {
		return nombre; 
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof Enfermedad))
			return false;
		Enfermedad that = (Enfermedad) o;
		return Objects.equals(codigo_sick, that.codigo_sick);
	}
}