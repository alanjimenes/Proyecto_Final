package logico;

import java.io.Serializable;

public class User implements Serializable{

	private static final long serialVersionUID = 1L;
	private String tipo;
	private String usuario;
	private String password;

	public User(String tipo, String usuario, String password) {
		super();
		this.tipo = tipo;
		this.usuario = usuario;
		this.password = password;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}



}

