package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;

import Utils.ConexionDB;
import logico.User;

public class UserService {

    public User login(String usuario, String password) {
        User user = null;

        String sql = "select nombreUsuario, password, rol from Usuario where nombreUsuario = ? and password = ? ";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(rs.getString("rol"), rs.getString("nombreUsuario"), rs.getString("password"), "");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

    public boolean existeUsuario(String usuario) {
        String sql = "select codigo_usuario from Usuario where nombreUsuario = ? ";

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
        String sqlUser = "insert into Usuario (nombreUsuario, password, rol) values (?, ?, ?)";

        String sqlPersona = " select codigo_persona from Persona where cedula = ? ";

        String sqlUpdate = " update Medico set codigo_usuario = ? where codigo_persona = ?";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            int codigoUsuario = 0;

            try (PreparedStatement stmtUser = conn.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                stmtUser.setString(1, user.getUsuario());
                stmtUser.setString(2, user.getPassword());
                stmtUser.setString(3, user.getRol());

                stmtUser.executeUpdate();

                try (ResultSet rs = stmtUser.getGeneratedKeys()) {
                    if (rs.next()) {
                        codigoUsuario = rs.getInt(1);
                    }
                }
            }

            // 2. Validar lógica adicional si es Médico
            if (user.getRol().equalsIgnoreCase("Medico")) {
                int codigoPersona = 0;

                try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                    stmtPersona.setString(1, user.getCedula());

                    try (ResultSet rs = stmtPersona.executeQuery()) {
                        if (rs.next()) {
                            codigoPersona = rs.getInt("codigo_persona");
                        } else {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                    stmtUpdate.setInt(1, codigoUsuario);
                    stmtUpdate.setInt(2, codigoPersona);
                    stmtUpdate.executeUpdate();
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

        String sql = "select nombreUsuario, password, rol from Usuario order by nombreUsuario";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new User(rs.getString("rol"), rs.getString("nombreUsuario"), rs.getString("password"), ""));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean eliminarUsuario(String usuario) {
        String sql = "delete from Usuario where nombreUsuario = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean actualizarUsuario(User user) {
        String sql = "update Usuario set password = ?, rol = ? where nombreUsuario = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getRol());
            stmt.setString(3, user.getUsuario());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}