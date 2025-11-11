package logico;

public class Especialidad {
	private String codigo_espe;
	private String nombre;
	private String descripcion;

	public Especialidad(String codigo_espe, String nombre, String descripcion) {
		super();
		this.codigo_espe = codigo_espe;
		this.nombre = nombre;
		this.descripcion = descripcion;
	}
	public void setCodigo_espe(String codigo_espe) {
		this.codigo_espe = codigo_espe;
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


	public String getCodigo_espe() {
		return codigo_espe;
	}


	

	
}
