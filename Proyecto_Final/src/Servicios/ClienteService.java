package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

import logico.Cliente;
import Utils.ConexionDB;

public class ClienteService {

    public boolean registrarNuevoCliente(Cliente cli) {
        String sqlPersona = "insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero) values " +
                "(?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlCliente = "insert into cliente (codigo_persona, numexpediente, enfermo) values " +
                "(?, ?, ?)";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            int idPersona = 0;

            try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtPersona.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
                stmtPersona.setString(2, cli.getNombre());
                stmtPersona.setString(3, cli.getApellido());
                stmtPersona.setString(4, cli.getCedula());
                stmtPersona.setString(5, cli.getTelefono());
                stmtPersona.setBoolean(6, cli.getEstado());
                stmtPersona.setString(7, cli.getDireccion());
                stmtPersona.setString(8, cli.getGenero());

                stmtPersona.executeUpdate();

                try (ResultSet rs = stmtPersona.getGeneratedKeys()) {
                    if (rs.next()) {
                        idPersona = rs.getInt(1);
                    }
                }
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setInt(1, idPersona);

                String exp = cli.getNumExpediente();
                if (exp == null || exp.isEmpty() || exp.equals("N/A")) {
                    exp = "EXP-" + idPersona;
                }

                stmtCliente.setString(2, exp);
                stmtCliente.setBoolean(3, cli.isEnfermo());
                stmtCliente.executeUpdate();
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

    public boolean actualizarCliente(Cliente cli) {
        String sqlPersona = "update persona set fechanacimiento = ?, nombre = ?, " +
                "apellido = ?, telefono = ?, direccion = ?, estado = ?, genero = ? " +
                "where cedula = ?";

        String sqlCliente = "update cliente set enfermo = ?, numexpediente = ? " +
                "where codigo_persona = (" +
                "select codigo_persona " +
                "from persona " +
                "where cedula = ?)";

        Connection conn = null;

        try {
            conn = ConexionDB.getConexion();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                stmtPersona.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
                stmtPersona.setString(2, cli.getNombre());
                stmtPersona.setString(3, cli.getApellido());
                stmtPersona.setString(4, cli.getTelefono());
                stmtPersona.setString(5, cli.getDireccion());
                stmtPersona.setBoolean(6, cli.getEstado());
                stmtPersona.setString(7, cli.getGenero());
                stmtPersona.setString(8, cli.getCedula()); // Cédula al final para el WHERE
                stmtPersona.executeUpdate();
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setBoolean(1, cli.isEnfermo());
                stmtCliente.setString(2, cli.getNumExpediente());
                stmtCliente.setString(3, cli.getCedula());
                stmtCliente.executeUpdate();
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

    public boolean desactivarCliente(String cedula) {
        String sql = "update persona set estado = 0 where cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cliente buscarClientePorCodigo(String codigoExpediente) {
        Cliente cliente = null;
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, " +
                "persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, " +
                "cliente.numexpediente, cliente.enfermo, persona.genero " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoExpediente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                            rs.getInt("codigo_persona"),
                            rs.getDate("fechanacimiento").toLocalDate(),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("cedula"),
                            rs.getString("telefono"),
                            rs.getBoolean("estado"),
                            rs.getString("direccion"),
                            rs.getString("genero"),
                            rs.getString("numexpediente"),
                            rs.getBoolean("enfermo"),
                            ""
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public Cliente buscarClientePorCedula(String cedula) {
        Cliente cliente = null;
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, " +
                "persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, cliente.numexpediente, " +
                "cliente.enfermo, persona.genero " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente(
                            rs.getInt("codigo_persona"),
                            rs.getDate("fechanacimiento").toLocalDate(),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("cedula"),
                            rs.getString("telefono"),
                            rs.getBoolean("estado"),
                            rs.getString("direccion"),
                            rs.getString("genero"),
                            rs.getString("numexpediente"),
                            rs.getBoolean("enfermo"),
                            ""
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, " +
                "persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, cliente.numexpediente, " +
                "cliente.enfermo, persona.genero " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(

                        rs.getInt("codigo_persona"),
                        rs.getDate("fechanacimiento").toLocalDate(),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("cedula"),
                        rs.getString("telefono"),
                        rs.getBoolean("estado"),
                        rs.getString("direccion"),
                        rs.getString("genero"),
                        rs.getString("numexpediente"),
                        rs.getBoolean("enfermo"),
                        ""
                );
                lista.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}