package logico;

public class Especialidad {
	private String codigo_espe;
	private String nombre;

	public Especialidad(String codigo_espe, String nombre) {
		super();
		this.codigo_espe = codigo_espe;
		this.nombre = nombre;
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

	public String getCodigo_espe() {
		return codigo_espe;
	}
	
}
