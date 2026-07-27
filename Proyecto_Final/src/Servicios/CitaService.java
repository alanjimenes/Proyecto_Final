package Servicios;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import logico.Cita;
import Utils.ConexionDB;
import logico.Cliente;
import logico.Medico;

public class CitaService {

    public boolean crearCita(Cita cita, String cedulaMedico, String cedulaCliente) {
        String sql = "insert into cita (codigo_medico, codigo_cliente, fechacita, estado, motivo) values ((select persona.codigo_persona from persona where persona.cedula = ?), (select persona.codigo_persona from persona where persona.cedula = ?), ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaMedico);
            stmt.setString(2, cedulaCliente);
            stmt.setTimestamp(3, Timestamp.valueOf(cita.getFechaHora()));
            stmt.setString(4, cita.getEstado());
            stmt.setString(5, cita.getMotivo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editCita(int codigoCita, LocalDateTime nuevaFechaHora, String cedulaMedico) {
        String sql = "update cita set cita.fechacita = ?, cita.codigo_medico = (select persona.codigo_persona from persona where persona.cedula = ?) where cita.codigo_cita = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(nuevaFechaHora));
            stmt.setString(2, cedulaMedico);
            stmt.setInt(3, codigoCita);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelCita(int codigoCita) {
        String sql = "update cita set cita.estado = 'Cancelada' where cita.codigo_cita = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoCita);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int contarCitasPorDia(int codigoMedico, LocalDate fecha) {
        int total = 0;
        String sql = "select count(cita.codigo_cita) AS total from cita where cita.codigo_medico = ? and cast(cita.fechacita AS date) = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoMedico);
            stmt.setDate(2, java.sql.Date.valueOf(fecha));
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public int contarCitasPorMes(int mes, int anio) {
        int total = 0;
        String sql = "select count(cita.codigo_cita) AS total from cita where month(cita.fechacita) = ? and year(cita.fechacita) = ? and cita.estado = 'Completada'";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, mes);
            stmt.setInt(2, anio);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                total = rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public Cita buscarCita(int codigoCita) {
        Cita cita = null;
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "pCli.cedula as cli_ced, pCli.nombre as cli_nom, pCli.apellido as cli_ape, cliente.numexpediente, " +
                "pMed.cedula as med_ced, pMed.nombre as med_nom, pMed.apellido as med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona pCli on cliente.codigo_persona = pCli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona pMed on medico.codigo_persona = pMed.codigo_persona " +
                "where cita.codigo_cita = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoCita);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cli = new Cliente(rs.getString("cli_ced"), rs.getString("cli_nom"), rs.getString("cli_ape"), "", LocalDate.now(), "", true, rs.getString("numexpediente"), null, false, null, "");
                logico.Especialidad espDummy = new logico.Especialidad("0", "N/A");
                Medico med = new Medico(rs.getString("med_ced"), rs.getString("med_nom"), rs.getString("med_ape"), LocalDate.now(), "", "", true, espDummy, 0, null, null);

                cita = new Cita(
                        rs.getTimestamp("fechacita").toLocalDateTime(),
                        cli,
                        med,
                        rs.getString("estado"),
                        rs.getString("motivo")
                );
                cita.setCodigo_cita(String.valueOf(rs.getInt("codigo_cita")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cita;
    }

    public ArrayList<Cita> getTodasLasCitas() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "pCli.cedula as cli_ced, pCli.nombre as cli_nom, pCli.apellido as cli_ape, " +
                "pMed.cedula as med_ced, pMed.nombre as med_nom, pMed.apellido as med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona pCli on cliente.codigo_persona = pCli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona pMed on medico.codigo_persona = pMed.codigo_persona";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cli = new Cliente(rs.getString("cli_ced"), rs.getString("cli_nom"), rs.getString("cli_ape"), "", LocalDate.now(), "", true, "", null, false, null, "");
                logico.Especialidad espDummy = new logico.Especialidad("0", "N/A");
                Medico med = new Medico(rs.getString("med_ced"), rs.getString("med_nom"), rs.getString("med_ape"), LocalDate.now(), "", "", true, espDummy, 0, null, null);

                Cita cita = new Cita(
                        rs.getTimestamp("fechacita").toLocalDateTime(),
                        cli,
                        med,
                        rs.getString("estado"),
                        rs.getString("motivo")
                );
                cita.setCodigo_cita(String.valueOf(rs.getInt("codigo_cita")));
                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Cita> getCitasPorMedico(String cedulaMedico) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "pCli.cedula as cli_ced, pCli.nombre as cli_nom, pCli.apellido as cli_ape, cliente.numexpediente, " +
                "pMed.cedula as med_ced, pMed.nombre as med_nom, pMed.apellido as med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona pCli on cliente.codigo_persona = pCli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona pMed on medico.codigo_persona = pMed.codigo_persona " +
                "where pMed.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cli = new Cliente(rs.getString("cli_ced"), rs.getString("cli_nom"), rs.getString("cli_ape"), "", LocalDate.now(), "", true, rs.getString("numexpediente"), null, false, null, "");
                logico.Especialidad espDummy = new logico.Especialidad("0", "N/A");
                Medico med = new Medico(rs.getString("med_ced"), rs.getString("med_nom"), rs.getString("med_ape"), LocalDate.now(), "", "", true, espDummy, 0, null, null);

                Cita cita = new Cita(
                        rs.getTimestamp("fechacita").toLocalDateTime(),
                        cli,
                        med,
                        rs.getString("estado"),
                        rs.getString("motivo")
                );
                cita.setCodigo_cita(String.valueOf(rs.getInt("codigo_cita")));
                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Cita> getCitasDeCliente(String numExpediente) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.codigo_medico, cita.codigo_cliente, cita.fechacita, cita.estado, cita.motivo from cita inner join cliente on cita.codigo_cliente = cliente.codigo_persona where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numExpediente);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cita cita = new Cita(
                        rs.getTimestamp("fechacita").toLocalDateTime(),
                        null,
                        null,
                        rs.getString("estado"),
                        rs.getString("motivo")
                );
                cita.setCodigo_cita(String.valueOf(rs.getInt("codigo_cita")));
                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Cita> getCitasPorRango(LocalDateTime desde, LocalDateTime hasta) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.codigo_medico, cita.codigo_cliente, cita.fechacita, cita.estado, cita.motivo from cita where cita.fechacita >= ? and cita.fechacita <= ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(desde));
            stmt.setTimestamp(2, Timestamp.valueOf(hasta));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cita cita = new Cita(
                        rs.getTimestamp("fechacita").toLocalDateTime(),
                        null,
                        null,
                        rs.getString("estado"),
                        rs.getString("motivo")
                );
                cita.setCodigo_cita(String.valueOf(rs.getInt("codigo_cita")));
                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}