package logico;

import java.io.Serializable;

public class User implements Serializable {
	private int codigoUsuario;
	private String nombreUsuario;
	private String password;
	private String rol;

	public User(int codigoUsuario, String nombreUsuario, String password, String rol) {
		this.codigoUsuario = codigoUsuario;
		this.nombreUsuario = nombreUsuario;
		this.password = password;
		this.rol = rol;
	}

	public int getCodigoUsuario() { return codigoUsuario; }
	public void setCodigoUsuario(int codigoUsuario) { this.codigoUsuario = codigoUsuario; }
	public String getNombreUsuario() { return nombreUsuario; }
	public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
	public String getRol() { return rol; }
	public void setRol(String rol) { this.rol = rol; }
}