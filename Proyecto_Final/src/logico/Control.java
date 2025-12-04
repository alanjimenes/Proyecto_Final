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
		if (user != null) {
			misUsuarios.add(user);
		}
	}

	public boolean confirmLogin(String username, String password) {
		if (username == null || password == null) {
			return false;
		}

		boolean login = false;
		for (User user : misUsuarios) {
			if (user != null && user.getUsuario().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
				loginUser = user;
				login = true;
				break;
			}
		}
		return login;
	}

	public boolean userExist(String username) {
		if (username == null)
			return false;

		for (User user : misUsuarios) {
			if (user.getUsuario().equalsIgnoreCase(username)) {
				return true;
			}
		}
		return false;
	}

	public static User getLoginUser() {
		return loginUser;
	}

	public static void setLoginUser(User loginUser) {
		Control.loginUser = loginUser;
	}
}