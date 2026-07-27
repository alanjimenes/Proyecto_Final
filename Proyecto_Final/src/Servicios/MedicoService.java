package Servicios;

import logico.Especialidad;
import logico.Medico;
import Utils.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MedicoService {

    public boolean agregarMedico(Medico med, int codigoUsuario, int codigoEspecialidad) {
        String sqlPersona = "insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion) values (?, ?, ?, ?, ?, ?, ?)";
        String sqlMedico = "insert into medico (codigo_persona, codigo_usuario, codigo_especialidad, maxcitaspordia) values (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            stmtPersona.setDate(1, Date.valueOf(med.getFechaNacimiento()));
            stmtPersona.setString(2, med.getNombre());
            stmtPersona.setString(3, med.getApellido());
            stmtPersona.setString(4, med.getCedula());
            stmtPersona.setString(5, med.getTelefono());
            stmtPersona.setBoolean(6, med.isActivo());
            stmtPersona.setString(7, med.getDireccion());

            stmtPersona.executeUpdate();
            ResultSet rs = stmtPersona.getGeneratedKeys();
            int idPersona = 0;
            if (rs.next()) {
                idPersona = rs.getInt(1);
            }

            try (PreparedStatement stmtMedico = conn.prepareStatement(sqlMedico)) {
                stmtMedico.setInt(1, idPersona);

                if (codigoUsuario == 0) {
                    stmtMedico.setNull(2, java.sql.Types.INTEGER);
                } else {
                    stmtMedico.setInt(2, codigoUsuario);
                }

                stmtMedico.setInt(3, codigoEspecialidad);
                stmtMedico.setInt(4, med.getMaxCitasPorDia());
                stmtMedico.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarMedico(Medico med) {
        String sqlPersona = "update persona set persona.fechanacimiento = ?, persona.nombre = ?, persona.apellido = ?, persona.telefono = ?, persona.direccion = ?, persona.estado = ? where persona.cedula = ?";
        String sqlMedico = "update medico set medico.codigo_especialidad = ?, medico.maxcitaspordia = ? where medico.codigo_persona = (select persona.codigo_persona from persona where persona.cedula = ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                stmtPersona.setDate(1, Date.valueOf(med.getFechaNacimiento()));
                stmtPersona.setString(2, med.getNombre());
                stmtPersona.setString(3, med.getApellido());
                stmtPersona.setString(4, med.getTelefono());
                stmtPersona.setString(5, med.getDireccion());
                stmtPersona.setBoolean(6, med.isActivo());
                stmtPersona.setString(7, med.getCedula());
                stmtPersona.executeUpdate();
            }

            try (PreparedStatement stmtMedico = conn.prepareStatement(sqlMedico)) {
                stmtMedico.setInt(1, Integer.parseInt(med.getEspecialidad().getCodigo_espe()));
                stmtMedico.setInt(2, med.getMaxCitasPorDia());
                stmtMedico.setString(3, med.getCedula());
                stmtMedico.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean desactivarMedico(String cedula) {
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

    public Medico buscarMedicoCedula(String cedula) {
        Medico medico = null;
        String sql = "select persona.fechanacimiento, persona.nombre, persona.apellido, persona.cedula, persona.telefono, persona.estado, persona.direccion, medico.maxcitaspordia, especialidad.codigo_especialidad, especialidad.nombre AS nombre_esp from medico inner join persona on medico.codigo_persona = persona.codigo_persona inner join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Especialidad esp = new Especialidad(
                        String.valueOf(rs.getInt("codigo_especialidad")),
                        rs.getString("nombre_esp")
                );

                medico = new Medico(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDate("fechanacimiento").toLocalDate(),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBoolean("estado"),
                        esp,
                        rs.getInt("maxcitaspordia"),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medico;
    }

    public ArrayList<Medico> listarMedicos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "select persona.fechanacimiento, persona.nombre, persona.apellido, persona.cedula, persona.telefono, persona.estado, persona.direccion, medico.maxcitaspordia, especialidad.codigo_especialidad, especialidad.nombre AS nombre_esp from medico inner join persona on medico.codigo_persona = persona.codigo_persona inner join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad(
                        String.valueOf(rs.getInt("codigo_especialidad")),
                        rs.getString("nombre_esp")
                );

                Medico medico = new Medico(
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getDate("fechanacimiento").toLocalDate(),
                        rs.getString("telefono"),
                        rs.getString("direccion"),
                        rs.getBoolean("estado"),
                        esp,
                        rs.getInt("maxcitaspordia"),
                        new ArrayList<>(),
                        new ArrayList<>()
                );
                lista.add(medico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean verificarDisponibilidad(String cedula, LocalDateTime fechaHora, LocalDateTime finHora) {
        boolean disponible = false;
        String sql = "select count(cita.codigo_cita) AS solapamientos from cita inner join medico on cita.codigo_medico = medico.codigo_persona inner join persona on medico.codigo_persona = persona.codigo_persona where persona.cedula = ? and cita.estado = 'Pendiente' and (cita.fechacita < ? and dateadd(minute, 30, cita.fechacita) > ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            stmt.setTimestamp(2, Timestamp.valueOf(finHora));
            stmt.setTimestamp(3, Timestamp.valueOf(fechaHora));

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (rs.getInt("solapamientos") == 0) {
                    disponible = true;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return disponible;
    }
}