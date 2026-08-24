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
        String sql = "{call sp_crear_cita(?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

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
        String sql = "{call sp_editar_cita(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoCita);
            stmt.setTimestamp(2, Timestamp.valueOf(nuevaFechaHora));
            stmt.setString(3, cedulaMedico);

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cancelCita(int codigoCita) {
        String sql = "{call sp_cancelar_cita(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoCita);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public int contarCitasPorDia(int codigoMedico, LocalDate fecha) {
        int total = 0;
        String sql = "select count(cita.codigo_cita) AS total " +
                "from cita " +
                "where cita.codigo_medico = ? and cast(cita.fechacita AS date) = ?";

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
        String sql = "select count(cita.codigo_cita) AS total " +
                "from cita " +
                "where month(cita.fechacita) = ? and year(cita.fechacita) = ? and cita.estado = 'Completada'";

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
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, persona_cli.apellido AS cli_ape, " +
                "cliente.numexpediente, persona_med.cedula AS med_ced, persona_med.nombre AS med_nom, persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where cita.codigo_cita = ?";

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
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, persona_cli.apellido AS cli_ape, " +
                "cliente.numexpediente, persona_med.cedula AS med_ced, persona_med.nombre AS med_nom, " +
                "persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona";

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
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, " +
                "persona_cli.apellido AS cli_ape, cliente.numexpediente, persona_med.cedula AS med_ced, " +
                "persona_med.nombre AS med_nom, persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where persona_med.cedula = ?";

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
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, " +
                "persona_cli.apellido AS cli_ape, cliente.numexpediente, persona_med.cedula AS med_ced, " +
                "persona_med.nombre AS med_nom, persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where cliente.numexpediente = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, numExpediente);
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

    public ArrayList<Cita> getCitasPorRango(LocalDateTime desde, LocalDateTime hasta) {
        ArrayList<Cita> lista = new ArrayList<>();
        String sql = "select cita.codigo_cita, cita.fechacita, cita.estado, cita.motivo, " +
                "persona_cli.cedula AS cli_ced, persona_cli.nombre AS cli_nom, " +
                "persona_cli.apellido AS cli_ape, cliente.numexpediente, persona_med.cedula AS med_ced, " +
                "persona_med.nombre AS med_nom, persona_med.apellido AS med_ape " +
                "from cita " +
                "inner join cliente on cita.codigo_cliente = cliente.codigo_persona " +
                "inner join persona persona_cli on cliente.codigo_persona = persona_cli.codigo_persona " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona persona_med on medico.codigo_persona = persona_med.codigo_persona " +
                "where cita.fechacita >= ? and cita.fechacita <= ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(desde));
            stmt.setTimestamp(2, Timestamp.valueOf(hasta));
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
}