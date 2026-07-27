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
        String sqlPersona = "insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion) values (?, ?, ?, ?, ?, ?, ?)";
        String sqlCliente = "insert into cliente (codigo_persona, numexpediente, enfermo, genero) values (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            stmtPersona.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
            stmtPersona.setString(2, cli.getNombre());
            stmtPersona.setString(3, cli.getApellido());
            stmtPersona.setString(4, cli.getCedula());
            stmtPersona.setString(5, cli.getTelefono());
            stmtPersona.setBoolean(6, cli.isActivo());
            stmtPersona.setString(7, cli.getDireccion());

            stmtPersona.executeUpdate();
            ResultSet rs = stmtPersona.getGeneratedKeys();
            int idPersona = 0;
            if (rs.next()) {
                idPersona = rs.getInt(1);
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setInt(1, idPersona);

                String exp = cli.getNumExpediente();
                if (exp == null || exp.isEmpty() || exp.equals("N/A")) {
                    exp = "EXP-" + idPersona;
                }

                stmtCliente.setString(2, exp);
                stmtCliente.setBoolean(3, cli.isEnfermo());
                stmtCliente.setString(4, cli.getGenero());
                stmtCliente.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCliente(Cliente cli) {
        String sqlPersona = "update persona set persona.fechanacimiento = ?, persona.nombre = ?, persona.apellido = ?, persona.telefono = ?, persona.direccion = ?, persona.estado = ? where persona.cedula = ?";
        String sqlCliente = "update cliente set cliente.enfermo = ?, cliente.numexpediente = ?, cliente.genero = ? where cliente.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                stmtPersona.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
                stmtPersona.setString(2, cli.getNombre());
                stmtPersona.setString(3, cli.getApellido());
                stmtPersona.setString(4, cli.getTelefono());
                stmtPersona.setString(5, cli.getDireccion());
                stmtPersona.setBoolean(6, cli.isActivo());
                stmtPersona.setString(7, cli.getCedula());
                stmtPersona.executeUpdate();
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setBoolean(1, cli.isEnfermo());
                stmtCliente.setString(2, cli.getNumExpediente());
                stmtCliente.setString(3, cli.getGenero());
                stmtCliente.setString(4, cli.getCedula());
                stmtCliente.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean desactivarCliente(String cedula) {
        String sql = "update persona set persona.estado = 0 where persona.cedula = ?";
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
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, cliente.numexpediente, cliente.enfermo, cliente.genero from cliente inner join persona on cliente.codigo_persona = persona.codigo_persona where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoExpediente);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getDate("fechanacimiento").toLocalDate(),
                        rs.getString("direccion"),
                        rs.getBoolean("estado"),
                        rs.getString("numexpediente"),
                        null,
                        rs.getBoolean("enfermo"),
                        new ArrayList<>(),
                        rs.getString("genero")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public Cliente buscarClientePorCedula(String cedula) {
        Cliente cliente = null;
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, cliente.numexpediente, cliente.enfermo, cliente.genero from cliente inner join persona on cliente.codigo_persona = persona.codigo_persona where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getDate("fechanacimiento").toLocalDate(),
                        rs.getString("direccion"),
                        rs.getBoolean("estado"),
                        rs.getString("numexpediente"),
                        null,
                        rs.getBoolean("enfermo"),
                        new ArrayList<>(),
                        rs.getString("genero")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public ArrayList<Cliente> getClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, persona.cedula, persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, cliente.numexpediente, cliente.enfermo, cliente.genero from cliente inner join persona on cliente.codigo_persona = persona.codigo_persona";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getDate("fechanacimiento").toLocalDate(),
                        rs.getString("direccion"),
                        rs.getBoolean("estado"),
                        rs.getString("numexpediente"),
                        null,
                        rs.getBoolean("enfermo"),
                        new ArrayList<>(),
                        rs.getString("genero")
                );
                lista.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}