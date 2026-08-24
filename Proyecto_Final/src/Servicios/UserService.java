package Servicios;

import java.sql.CallableStatement;
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
        String sql = "select usuario.codigo_usuario, usuario.nombreusuario, usuario.password, usuario.rol, " +
                "coalesce(p_med.cedula, p_enf.cedula) as cedula " +
                "from usuario " +
                "left join medico on usuario.codigo_usuario = medico.codigo_usuario " +
                "left join persona p_med on medico.codigo_persona = p_med.codigo_persona " +
                "left join enfermera on usuario.codigo_usuario = enfermera.codigo_usuario " +
                "left join persona p_enf on enfermera.codigo_persona = p_enf.codigo_persona " +
                "where usuario.nombreusuario = ? and usuario.password = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setCodigoUsuario(rs.getInt("codigo_usuario"));
                    user.setNombreUsuario(rs.getString("nombreusuario"));
                    user.setPassword(rs.getString("password"));
                    user.setRol(rs.getString("rol"));
                    user.setCedula(rs.getString("cedula"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean existeUsuario(String usuario) {
        String sql = "select usuario.codigo_usuario " +
                "from usuario " +
                "where usuario.nombreusuario = ?";

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
        String sql = "{call sp_crear_usuario(?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, user.getNombreUsuario());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRol());
            stmt.setString(4, user.getCedula());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<User> listarUsuarios() {
        ArrayList<User> lista = new ArrayList<>();
        String sql = "select usuario.codigo_usuario, usuario.nombreusuario, usuario.password, usuario.rol, " +
                "coalesce(p_med.cedula, p_enf.cedula) as cedula " +
                "from usuario " +
                "left join medico on usuario.codigo_usuario = medico.codigo_usuario " +
                "left join persona p_med on medico.codigo_persona = p_med.codigo_persona " +
                "left join enfermera on usuario.codigo_usuario = enfermera.codigo_usuario " +
                "left join persona p_enf on enfermera.codigo_persona = p_enf.codigo_persona " +
                "order by usuario.nombreusuario";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                User user = new User();
                user.setCodigoUsuario(rs.getInt("codigo_usuario"));
                user.setNombreUsuario(rs.getString("nombreusuario"));
                user.setPassword(rs.getString("password"));
                user.setRol(rs.getString("rol"));
                user.setCedula(rs.getString("cedula"));
                lista.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean eliminarUsuario(String usuario) {
        String sql = "{call sp_eliminar_usuario(?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarUsuario(User user) {
        String sql = "{call sp_editar_usuario(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, user.getNombreUsuario());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRol());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}