package logico;

public class Enfermedad {
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


}