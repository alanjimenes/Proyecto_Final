package Servicios;

import java.sql.*;
import java.util.ArrayList;

import logico.*;
import Utils.ConexionDB;

public class ClienteService {

    public boolean registrarNuevoCliente(Cliente cli) {
        String sqlPersona = "insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero) values (?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlCliente = "insert into cliente (codigo_persona, numexpediente, enfermo, antecedentes) values (?, ?, ?, ?)";

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
                stmtCliente.setString(4, cli.getAntecedentes());
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
        String sqlPersona = "update persona set fechanacimiento = ?, nombre = ?, apellido = ?, telefono = ?, direccion = ?, estado = ?, genero = ? where cedula = ?";
        String sqlCliente = "update cliente set enfermo = ?, numexpediente = ?, antecedentes = ? " +
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
                stmtPersona.setString(8, cli.getCedula());
                stmtPersona.executeUpdate();
            }

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setBoolean(1, cli.isEnfermo());
                stmtCliente.setString(2, cli.getNumExpediente());
                stmtCliente.setString(3, cli.getAntecedentes());
                stmtCliente.setString(4, cli.getCedula());
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

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

        String sql = "{CALL sp_desactivar_persona(?)}";

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
        String sql = "{CALL sp_activar_persona(?)}";

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