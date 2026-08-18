package Servicios;

import Utils.ConexionDB;
import logico.Cita;
import logico.Cliente;
import logico.Medico;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class CitaService {

    public boolean crearCita(Cita cita, String cedulaMedico, String cedulaCliente) {
        String sql = "insert into cita (codigo_medico, codigo_cliente, fechacita, estado, motivo) " +
                "values ((select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?), " +
                "(select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?), ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaMedico);
            stmt.setString(2, cedulaCliente);
            stmt.setTimestamp(3, Timestamp.valueOf(cita.getFechaCita()));
            stmt.setString(4, cita.getEstado());
            stmt.setString(5, cita.getMotivo());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editCita(int codigoCita, LocalDateTime nuevaFechaHora, String cedulaMedico) {
        String sql = "update cita set fechacita = ?, " +
                "codigo_medico = (" +
                "select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?) " +
                "where codigo_cita = ?";
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
        String sql = "update cita set estado = 'Cancelada' " +
                "where codigo_cita = ?";
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
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, pcli.cedula AS cli_ced, pcli.nombre AS cli_nom, pcli.apellido AS cli_ape, cliente.numexpediente, pmed.cedula AS med_ced, pmed.nombre AS med_nom, pmed.apellido AS med_ape from cita inner join cliente on cita.codigo_cliente = cliente.codigo_persona inner join persona pcli on cliente.codigo_persona = pcli.codigo_persona inner join medico on cita.codigo_medico = medico.codigo_persona inner join persona pmed on medico.codigo_persona = pmed.codigo_persona where cita.codigo_cita = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoCita);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cita;
    }

    public ArrayList<Cita> getTodasLasCitas() {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, pcli.cedula AS cli_ced, pcli.nombre AS cli_nom, pcli.apellido AS cli_ape, cliente.numexpediente, pmed.cedula AS med_ced, pmed.nombre AS med_nom, pmed.apellido AS med_ape from cita inner join cliente on cita.codigo_cliente = cliente.codigo_persona inner join persona pcli on cliente.codigo_persona = pcli.codigo_persona inner join medico on cita.codigo_medico = medico.codigo_persona inner join persona pmed on medico.codigo_persona = pmed.codigo_persona";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));

                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Cita> getCitasPorMedico(String cedulaMedico) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, pcli.cedula AS cli_ced, pcli.nombre AS cli_nom, pcli.apellido AS cli_ape, cliente.numexpediente, pmed.cedula AS med_ced, pmed.nombre AS med_nom, pmed.apellido AS med_ape from cita inner join cliente on cita.codigo_cliente = cliente.codigo_persona inner join persona pcli on cliente.codigo_persona = pcli.codigo_persona inner join medico on cita.codigo_medico = medico.codigo_persona inner join persona pmed on medico.codigo_persona = pmed.codigo_persona where pmed.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedulaMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cli = new Cliente();
                cli.setCedula(rs.getString("cli_ced"));
                cli.setNombre(rs.getString("cli_nom"));
                cli.setApellido(rs.getString("cli_ape"));
                cli.setNumExpediente(rs.getString("numexpediente"));

                Medico med = new Medico();
                med.setCedula(rs.getString("med_ced"));
                med.setNombre(rs.getString("med_nom"));
                med.setApellido(rs.getString("med_ape"));

                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setCliente(cli);
                cita.setMedico(med);
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));

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
                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));
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
                Cita cita = new Cita();
                cita.setCodigoCita(rs.getInt("codigo_cita"));
                cita.setFechaCita(rs.getTimestamp("fechacita").toLocalDateTime());
                cita.setEstado(rs.getString("estado"));
                cita.setMotivo(rs.getString("motivo"));
                lista.add(cita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}