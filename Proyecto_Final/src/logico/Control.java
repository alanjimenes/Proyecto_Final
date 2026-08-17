package logico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Control {

	private static Control control;
	private User loginUser;

	private Control() {
	}

	public static Control getInstance() {
		if (control == null) {
			control = new Control();
		}
		return control;
	}

	public User getLoginUser() {
		return loginUser;
	}

	public boolean confirmLogin(Connection conn, String username, String password) {
		boolean login = false;
		String query = "select * from usuario where nombreusuario = ? and password = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			ps.setString(2, password);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					loginUser = new User(rs.getInt("codigo_usuario"), rs.getString("nombreusuario"), rs.getString("password"), rs.getString("rol"));
					login = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return login;
	}

	public boolean userExist(Connection conn, String username) {
		boolean existe = false;
		String query = "select 1 from usuario where nombreusuario = ?";

		try (PreparedStatement ps = conn.prepareStatement(query)) {
			ps.setString(1, username);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					existe = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return existe;
	}
}