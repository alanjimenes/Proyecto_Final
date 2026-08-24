package Servicios;

import java.sql.*;
import java.util.ArrayList;

import logico.*;
import Utils.ConexionDB;

public class ClienteService {

    public boolean registrarNuevoCliente(Cliente cli) {
        String sql = "{call sp_crear_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
            stmt.setString(2, cli.getNombre());
            stmt.setString(3, cli.getApellido());
            stmt.setString(4, cli.getCedula());
            stmt.setString(5, cli.getTelefono());
            stmt.setBoolean(6, cli.getEstado());
            stmt.setString(7, cli.getDireccion());
            stmt.setString(8, cli.getGenero());
            stmt.setString(9, cli.getNumExpediente());
            stmt.setBoolean(10, cli.isEnfermo());
            stmt.setString(11, cli.getAntecedentes());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarCliente(Cliente cli) {
        String sql = "{call sp_editar_cliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(cli.getFechaNacimiento()));
            stmt.setString(2, cli.getNombre());
            stmt.setString(3, cli.getApellido());
            stmt.setString(4, cli.getTelefono());
            stmt.setString(5, cli.getDireccion());
            stmt.setBoolean(6, cli.getEstado());
            stmt.setString(7, cli.getGenero());
            stmt.setString(8, cli.getCedula());
            stmt.setBoolean(9, cli.isEnfermo());
            stmt.setString(10, cli.getNumExpediente());
            stmt.setString(11, cli.getAntecedentes());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean desactivarCliente(String cedula) {
        String sql = "{call sp_eliminar_cliente(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Cliente buscarClientePorCodigo(String codigoExpediente) {
        Cliente cliente = null;
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.fechanacimiento, " +
                "persona.direccion, persona.estado, cliente.numexpediente, cliente.enfermo, " +
                "persona.genero, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoExpediente);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setCodigoPersona(rs.getInt("codigo_persona"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setCedula(rs.getString("cedula"));
                    cliente.setTelefono(rs.getString("telefono"));
                    if (rs.getDate("fechanacimiento") != null) {
                        cliente.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                    }
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setEstado(rs.getBoolean("estado"));
                    cliente.setGenero(rs.getString("genero"));
                    cliente.setNumExpediente(rs.getString("numexpediente"));
                    cliente.setEnfermo(rs.getBoolean("enfermo"));
                    cliente.setAntecedentes(rs.getString("antecedentes"));
                    cliente.setHistorial(new Historial());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }

    public Cliente buscarClientePorCedula(String cedula) {
        Cliente cliente = null;
        String sql = "select persona.codigo_persona, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.fechanacimiento, " +
                "persona.direccion, persona.estado, cliente.numexpediente, " +
                "cliente.enfermo, persona.genero, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setCodigoPersona(rs.getInt("codigo_persona"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setCedula(rs.getString("cedula"));
                    cliente.setTelefono(rs.getString("telefono"));
                    if (rs.getDate("fechanacimiento") != null) {
                        cliente.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                    }
                    cliente.setDireccion(rs.getString("direccion"));
                    cliente.setEstado(rs.getBoolean("estado"));
                    cliente.setGenero(rs.getString("genero"));
                    cliente.setNumExpediente(rs.getString("numexpediente"));
                    cliente.setEnfermo(rs.getBoolean("enfermo"));
                    cliente.setAntecedentes(rs.getString("antecedentes"));
                    cliente.setHistorial(new Historial());
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
                "persona.telefono, persona.fechanacimiento, persona.direccion, persona.estado, " +
                "cliente.numexpediente, cliente.enfermo, persona.genero, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setCodigoPersona(rs.getInt("codigo_persona"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setCedula(rs.getString("cedula"));
                cliente.setTelefono(rs.getString("telefono"));
                if (rs.getDate("fechanacimiento") != null) {
                    cliente.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                cliente.setDireccion(rs.getString("direccion"));
                cliente.setEstado(rs.getBoolean("estado"));
                cliente.setGenero(rs.getString("genero"));
                cliente.setNumExpediente(rs.getString("numexpediente"));
                cliente.setEnfermo(rs.getBoolean("enfermo"));
                cliente.setAntecedentes(rs.getString("antecedentes"));
                cliente.setHistorial(new Historial());
                lista.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Historial obtenerHistorialPorCliente(int codigoCliente) {
        Historial historial = null;

        String sqlHistorial = "select historial.codigo_historial, persona.codigo_persona, persona.nombre, " +
                "persona.apellido, persona.cedula, cliente.antecedentes " +
                "from historial " +
                "inner join cliente on historial.codigo_cliente = cliente.codigo_persona " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where historial.codigo_cliente = ?";

        try (Connection con = ConexionDB.getConexion(); PreparedStatement psHist = con.prepareStatement(sqlHistorial)) {

            psHist.setInt(1, codigoCliente);
            ResultSet rsHist = psHist.executeQuery();

            if (rsHist.next()) {
                historial = new Historial();
                historial.setCodigoHistorial(rsHist.getInt("codigo_historial"));

                Cliente cliente = new Cliente();
                cliente.setCodigoPersona(rsHist.getInt("codigo_persona"));
                cliente.setNombre(rsHist.getString("nombre"));
                cliente.setApellido(rsHist.getString("apellido"));
                cliente.setCedula(rsHist.getString("cedula"));
                cliente.setAntecedentes(rsHist.getString("antecedentes"));

                historial.setCliente(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return historial;
    }

    private ArrayList<Consulta> obtenerConsultasPorCliente(Connection con, int codigoCliente) {
        ArrayList<Consulta> lista = new ArrayList<>();

        String sqlConsultas = "select consulta.codigo_cons, consulta.fecha, consulta.sintomas, consulta.diagnostico, " +
                "medico.codigo_persona, persona.nombre AS nombremedico, persona.apellido AS apellidomedico " +
                "from consulta " +
                "inner join medico on consulta.codigo_medico = medico.codigo_persona " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "where consulta.codigo_cliente = ? " +
                "order by consulta.fecha desc";

        try (PreparedStatement ps = con.prepareStatement(sqlConsultas)) {
            ps.setInt(1, codigoCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta();
                consulta.setCodigoConsulta(rs.getInt("codigo_cons"));

                if (rs.getDate("fecha") != null) {
                    consulta.setFechaConsulta(rs.getDate("fecha").toLocalDate());
                }

                consulta.setSintomas(rs.getString("sintomas"));
                consulta.setDiagnostico(rs.getString("diagnostico"));

                Medico medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                medico.setNombre(rs.getString("nombremedico"));
                medico.setApellido(rs.getString("apellidomedico"));
                consulta.setMedico(medico);

                lista.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    public boolean desactivarPersonaSp(String cedula) {
        String sql = "{call sp_desactivar_persona(?)}";

        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedula);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean activarPersonaSp(String cedula) {
        String sql = "{call sp_activar_persona(?)}";

        try (Connection conn = ConexionDB.getConexion(); CallableStatement stmt = conn.prepareCall(sql)) {
            stmt.setString(1, cedula);
            stmt.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Cliente> obtenerTodosLosClientes() {
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.fechanacimiento, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.estado, persona.direccion, persona.genero, " +
                "cliente.numexpediente, cliente.enfermo, cliente.antecedentes " +
                "from cliente " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona";

        try (Connection con = ConexionDB.getConexion(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCodigoPersona(rs.getInt("codigo_persona"));

                java.sql.Date fechaSql = rs.getDate("fechanacimiento");
                if (fechaSql != null) {
                    cli.setFechaNacimiento(fechaSql.toLocalDate());
                }

                cli.setNombre(rs.getString("nombre"));
                cli.setApellido(rs.getString("apellido"));
                cli.setCedula(rs.getString("cedula"));
                cli.setTelefono(rs.getString("telefono"));
                cli.setEstado(rs.getBoolean("estado"));
                cli.setDireccion(rs.getString("direccion"));
                cli.setGenero(rs.getString("genero"));
                cli.setNumExpediente(rs.getString("numexpediente"));
                cli.setEnfermo(rs.getBoolean("enfermo"));
                cli.setAntecedentes(rs.getString("antecedentes"));

                lista.add(cli);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener la lista de clientes: " + e.getMessage());
            e.printStackTrace();
        }

        for (Cliente cli : lista) {
            Historial h = HistorialService.obtenerHistorialPorCedula(cli.getCedula());
            if (h != null) {
                cli.setHistorial(h);
            }
        }

        return lista;
    }
}