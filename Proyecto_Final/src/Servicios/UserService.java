package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Utils.ConexionDB;
import logico.User;

public class UserService {

    public User login(String usuario, String password) {
        User user = null;
        String sql = "select usuario.codigo_usuario, usuario.nombreusuario, usuario.password, usuario.rol, coalesce(p_med.cedula, p_enf.cedula) as cedula from usuario left join medico on usuario.codigo_usuario = medico.codigo_usuario left join persona p_med on medico.codigo_persona = p_med.codigo_persona left join enfermera on usuario.codigo_usuario = enfermera.codigo_usuario left join persona p_enf on enfermera.codigo_persona = p_enf.codigo_persona where usuario.nombreusuario = ? and usuario.password = ?";

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
        String sql = "select usuario.codigo_usuario from usuario where usuario.nombreusuario = ?";

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
        String sqlUpdateMedico = "update medico set medico.codigo_usuario = ? where medico.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = ?)";
        String sqlUpdateEnfermera = "update enfermera set enfermera.codigo_usuario = ? where enfermera.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = ?)";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);
            int idGenerado = -1;

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, user.getNombreUsuario());
                stmtUser.setString(2, user.getPassword());
                stmtUser.setString(3, user.getRol());
                stmtUser.executeUpdate();

                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                    }
                }
            }

            if (idGenerado != -1 && user.getCedula() != null && !user.getCedula().isEmpty()) {
                String sqlUpdate = null;
                if (user.getRol().equalsIgnoreCase("Medico")) {
                    sqlUpdate = sqlUpdateMedico;
                } else if (user.getRol().equalsIgnoreCase("Enfermera")) {
                    sqlUpdate = sqlUpdateEnfermera;
                }

                if (sqlUpdate != null) {
                    try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                        stmtUpdate.setInt(1, idGenerado);
                        stmtUpdate.setString(2, user.getCedula());
                        stmtUpdate.executeUpdate();
                    }
                }
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
        String sql = "select usuario.codigo_usuario, usuario.nombreusuario, usuario.password, usuario.rol, coalesce(p_med.cedula, p_enf.cedula) as cedula from usuario left join medico on usuario.codigo_usuario = medico.codigo_usuario left join persona p_med on medico.codigo_persona = p_med.codigo_persona left join enfermera on usuario.codigo_usuario = enfermera.codigo_usuario left join persona p_enf on enfermera.codigo_persona = p_enf.codigo_persona order by usuario.nombreusuario";

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
        String sql = "delete from usuario where usuario.nombreusuario = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean actualizarUsuario(User user) {
        String sql = "update usuario set usuario.password = ?, usuario.rol = ? where usuario.nombreusuario = ?";

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