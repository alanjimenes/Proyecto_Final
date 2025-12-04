package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Control implements Serializable {

	private static final long serialVersionUID = 1L;
	private ArrayList<User> misUsuarios;
	private static Control control;
	private static User loginUser;

	private Control() {
		misUsuarios = new ArrayList<>();
	}

	public static Control getInstance() {
		if (control == null) {
			control = new Control();
		}
		return control;
	}

	public void regUser(User user) {
		if (misUsuarios == null) {
			misUsuarios = new ArrayList<>();
		}
		if (user != null) {
			System.out.println(">>> [SERVER] Registrando nuevo usuario: " + user.getUsuario());
			misUsuarios.add(user);
		}
	}

	public boolean confirmLogin(String username, String password) {
		if (username == null || password == null)
			return false;
		if (misUsuarios == null)
			return false;

		System.out.println("--- [SERVER] Intento de Login ---");
		System.out.println("Recibido: Usuario='" + username + "' Pass='" + password + "'");

		boolean login = false;
		for (User user : misUsuarios) {
			System.out.println("Comparando contra base de datos: Usuario='" + user.getUsuario() + "' Pass='"
					+ user.getPassword() + "'");

			if (user.getUsuario().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
				loginUser = user;
				login = true;
				System.out.println("¡MATCH! Login Exitoso.");
				break;
			}
		}
		if (!login)
			System.out.println("Login Fallido: No hubo coincidencia.");
		return login;
	}

	public boolean userExist(String username) {
		if (username == null)
			return false;
		if (misUsuarios == null)
			misUsuarios = new ArrayList<>();

		for (User user : misUsuarios) {
			if (user.getUsuario().equalsIgnoreCase(username)) {
				System.out.println("[SERVER] Conflicto: El usuario '" + username + "' YA EXISTE en la base de datos.");
				return true;
			}
		}
		return false;
	}

	public ArrayList<User> getMisUsuarios() {
		return misUsuarios;
	}

	public void setMisUsuarios(ArrayList<User> misUsuarios) {
		this.misUsuarios = misUsuarios;
	}

	public static Control getControl() {
		return control;
	}

	public static void setControl(Control control) {
		Control.control = control;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static User getLoginUser() {
		return loginUser;
	}

	public static void setLoginUser(User loginUser) {
		Control.loginUser = loginUser;
	}
}