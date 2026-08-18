package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Utils.ConexionDB;
import logico.User;

public class UserService {

    public User login(String usuario, String password) {
        User user = null;
        String sql = "select codigo_usuario, nombreusuario, password, rol from usuario where nombreusuario = ? and password = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getInt("codigo_usuario"), rs.getString("nombreusuario"), rs.getString("password"), rs.getString("rol"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean existeUsuario(String usuario) {
        String sql = "select codigo_usuario from usuario where nombreusuario = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean registrarUsuario(User user) {
        String sqlUser = "insert into usuario (nombreusuario, password, rol) values (?, ?, ?)";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser)) {
                stmtUser.setString(1, user.getNombreUsuario());
                stmtUser.setString(2, user.getPassword());
                stmtUser.setString(3, user.getRol());
                stmtUser.executeUpdate();
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public ArrayList<User> listarUsuarios() {
        ArrayList<User> lista = new ArrayList<>();
        String sql = "select codigo_usuario, nombreusuario, password, rol from usuario order by nombreusuario";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new User(rs.getInt("codigo_usuario"), rs.getString("nombreusuario"), rs.getString("password"), rs.getString("rol")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean eliminarUsuario(String usuario) {
        String sql = "delete from usuario where nombreusuario = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean actualizarUsuario(User user) {
        String sql = "update usuario set password = ?, rol = ? where nombreusuario = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getRol());
            stmt.setString(3, user.getNombreUsuario());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}