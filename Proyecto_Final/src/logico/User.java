package logico;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private int codigoUsuario;
	private String nombreUsuario;
	private String password;
	private String rol;
	private String cedula;

	public User() {
	}

	public User(int codigoUsuario, String nombreUsuario, String password, String rol) {
		this.codigoUsuario = codigoUsuario;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.rol = rol;
	}

	public User(int codigoUsuario, String nombreUsuario, String password, String rol, String cedula) {
		this.codigoUsuario = codigoUsuario;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.rol = rol;
		this.cedula = cedula;
	}

	public int getCodigoUsuario() { return codigoUsuario; }
	public void setCodigoUsuario(int codigoUsuario) { this.codigoUsuario = codigoUsuario; }

	public String getNombreUsuario() { return nombreUsuario; }
	public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }

	public String getRol() { return rol; }
	public void setRol(String rol) { this.rol = rol; }

	public String getCedula() { return cedula; }
	public void setCedula(String cedula) { this.cedula = cedula; }
}