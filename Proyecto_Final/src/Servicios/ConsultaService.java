package Servicios;

import Utils.ConexionDB;
import logico.Consulta;
import logico.Enfermedad;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class ConsultaService {


    public boolean registrarConsultaCompleta(Consulta con, String cedulaMedico, String cedulaCliente) {
        String sqlConsulta = "insert into consulta (fechaconsulta, sintomas, diagnostico, codigo_medico, codigo_cliente) values " +
                "(?, ?, ?, (" +
                "select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?), (" +
                "select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?))";
        String sqlEnfermedad = "insert into enfermedad_consulta (codigo_enfermedad, codigo_consulta) values " +
                "(?, ?)";

        try (Connection conn = Utils.ConexionDB.getConexion()) {
            conn.setAutoCommit(false);
            int generatedId = -1;

            try (PreparedStatement stmtCons = conn.prepareStatement(sqlConsulta, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmtCons.setTimestamp(1, Timestamp.valueOf(con.getFechaConsulta().atStartOfDay()));
                stmtCons.setString(2, con.getSintomas());
                stmtCons.setString(3, con.getDiagnostico());
                stmtCons.setString(4, cedulaMedico);
                stmtCons.setString(5, cedulaCliente);
                stmtCons.executeUpdate();

                ResultSet rs = stmtCons.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }

            if (generatedId != -1 && con.getEnfermedadesDiag() != null && !con.getEnfermedadesDiag().isEmpty()) {
                try (PreparedStatement stmtEnf = conn.prepareStatement(sqlEnfermedad)) {
                    for (Enfermedad enf : con.getEnfermedadesDiag()) {
                        stmtEnf.setInt(1, Integer.parseInt(enf.getCodigo_sick()));
                        stmtEnf.setInt(2, generatedId);
                        stmtEnf.addBatch();
                    }
                    stmtEnf.executeBatch();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int iniciarConsulta(Consulta con, int codigoMedico, int codigoCliente) {
        String sql = "insert into consulta (fechaconsulta, sintomas, diagnostico, codigo_medico, codigo_cliente) values " +
                "(?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = Utils.ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            stmt.setTimestamp(1, Timestamp.valueOf(con.getFechaConsulta().atStartOfDay()));
            stmt.setString(2, con.getSintomas());
            stmt.setString(3, con.getDiagnostico());
            stmt.setInt(4, codigoMedico);
            stmt.setInt(5, codigoCliente);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public boolean guardarConsulta(int codigoConsulta, String sintomas, String diagnostico, ArrayList<Enfermedad> enfermedades) {
        String sqlUpdate = "update consulta set consulta.sintomas = ?, consulta.diagnostico = ? " +
                "where consulta.codigo_cons = ?";
        String sqlEnfermedad = "insert into enfermedad_consulta (codigo_enfermedad, codigo_consulta) values " +
                "(?, ?)";

        try (Connection conn = Utils.ConexionDB.getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate)) {
                stmtUpdate.setString(1, sintomas);
                stmtUpdate.setString(2, diagnostico);
                stmtUpdate.setInt(3, codigoConsulta);
                stmtUpdate.executeUpdate();
            }

            if (enfermedades != null && !enfermedades.isEmpty()) {
                try (PreparedStatement stmtEnf = conn.prepareStatement(sqlEnfermedad)) {
                    for (Enfermedad enf : enfermedades) {
                        stmtEnf.setInt(1, Integer.parseInt(enf.getCodigo_sick()));
                        stmtEnf.setInt(2, codigoConsulta);
                        stmtEnf.addBatch();
                    }
                    stmtEnf.executeBatch();
                }
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Consulta> getTodasLasConsultas() {
        ArrayList<Consulta> lista = new ArrayList<>();
        String sql = "select consulta.codigo_cons, consulta.fechaconsulta, consulta.sintomas, consulta.diagnostico, " +
                "consulta.resumen " +
                "from consulta";

        try (Connection conn = Utils.ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        String.valueOf(rs.getInt("codigo_cons")),
                        rs.getTimestamp("fechaconsulta").toLocalDateTime().toLocalDate(),
                        rs.getString("sintomas"),
                        rs.getString("diagnostico"),
                        null,
                        null
                );
                consulta.setRecetaMedica(rs.getString("resumen"));
                lista.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Consulta> getConsultasPorRango(LocalDate desde, LocalDate hasta) {
        ArrayList<Consulta> lista = new ArrayList<>();
        String sql = "select consulta.codigo_cons, consulta.fechaconsulta, consulta.sintomas, consulta.diagnostico " +
                "from consulta " +
                "where consulta.fechaconsulta >= ? and consulta.fechaconsulta <= ?";

        try (Connection conn = Utils.ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(desde.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(hasta.atTime(23, 59, 59)));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        String.valueOf(rs.getInt("codigo_cons")),
                        rs.getTimestamp("fechaconsulta").toLocalDateTime().toLocalDate(),
                        rs.getString("sintomas"),
                        rs.getString("diagnostico"),
                        null,
                        null
                );
                lista.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Consulta> getConsultasPorDoctor(String cedulaDoctor) {
        ArrayList<Consulta> lista = new ArrayList<>();
        String sql = "select consulta.codigo_cons, consulta.fechaconsulta, consulta.sintomas, consulta.diagnostico " +
                "from consulta " +
                "inner join medico on consulta.codigo_medico = medico.codigo_persona " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = Utils.ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaDoctor);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        String.valueOf(rs.getInt("codigo_cons")),
                        rs.getTimestamp("fechaconsulta").toLocalDateTime().toLocalDate(),
                        rs.getString("sintomas"),
                        rs.getString("diagnostico"),
                        null,
                        null
                );
                lista.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Consulta> getConsultasPorCliente(String cedulaCliente) {
        ArrayList<Consulta> lista = new ArrayList<>();
        String sql = "select consulta.codigo_cons, consulta.fechaconsulta, consulta.sintomas, consulta.diagnostico " +
                "from consulta " +
                "inner join cliente on consulta.codigo_cliente = cliente.codigo_persona " +
                "inner join persona on cliente.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = Utils.ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaCliente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Consulta consulta = new Consulta(
                        String.valueOf(rs.getInt("codigo_cons")),
                        rs.getTimestamp("fechaconsulta").toLocalDateTime().toLocalDate(),
                        rs.getString("sintomas"),
                        rs.getString("diagnostico"),
                        null,
                        null
                );
                lista.add(consulta);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}