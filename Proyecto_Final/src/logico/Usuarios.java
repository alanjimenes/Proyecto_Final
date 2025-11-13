package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Usuarios implements Serializable{

	private static final long serialVersionUID = 1L;
	private ArrayList<User> misUsuarios;
	private static Usuarios usuarios;
	private static User loginUser;

	private Usuarios() {
		misUsuarios = new ArrayList<>();
	}

	public static Usuarios getInstance(){
		if(usuarios == null){
			usuarios = new Usuarios();
		}
		return usuarios;
	}

	public ArrayList<User> getMisUsuarios() {
		return misUsuarios;
	}

	public void setMisUsers(ArrayList<User> misUsers) {
		this.misUsuarios = misUsers;
	}

	public static Usuarios getUsuarios() {
		return usuarios;
	}

	public static void setControl(Usuarios control) {
		Usuarios.usuarios = control;
	}

	public static User getLoginUser() {
		return loginUser;
	}

	public static void setLoginUser(User loginUser) {
		Usuarios.loginUser = loginUser;
	}

	public void regUser(User user) {
		misUsuarios.add(user);

	}	

	public boolean confirmLogin(String text, String text2) {
		boolean login = false;
		for (User user : misUsuarios) {
			if(user.getUsuario().equals(text) && user.getPassword().equals(text2)){
				loginUser = user;
				login = true;
			}
		}
		return login;
	}

}