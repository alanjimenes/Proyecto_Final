package Servicios;

import logico.Especialidad;
import logico.Medico;
import Utils.ConexionDB;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class MedicoService {

    public boolean agregarMedico(Medico med, int codigoUsuario, int codigoEspecialidad) {
        String sql = "{call sp_crear_medico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(med.getFechaNacimiento()));
            stmt.setString(2, med.getNombre());
            stmt.setString(3, med.getApellido());
            stmt.setString(4, med.getCedula());
            stmt.setString(5, med.getTelefono());
            stmt.setBoolean(6, med.getEstado());
            stmt.setString(7, med.getDireccion());
            stmt.setString(8, med.getGenero());
            stmt.setInt(9, codigoUsuario);
            stmt.setInt(10, codigoEspecialidad);
            stmt.setInt(11, med.getMaxCitasPorDia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean actualizarMedico(Medico med) {
        String sql = "{call sp_editar_medico(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(med.getFechaNacimiento()));
            stmt.setString(2, med.getNombre());
            stmt.setString(3, med.getApellido());
            stmt.setString(4, med.getTelefono());
            stmt.setString(5, med.getDireccion());
            stmt.setBoolean(6, med.getEstado());
            stmt.setString(7, med.getGenero());
            stmt.setString(8, med.getCedula());
            stmt.setInt(9, med.getEspecialidad().getCodigoEspecialidad());
            stmt.setInt(10, med.getMaxCitasPorDia());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean desactivarMedico(String cedula) {
        String sql = "{call sp_desactivar_medico(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Medico buscarMedicoCedula(String cedula) {
        Medico medico = null;
        String sql = "select persona.codigo_persona, persona.fechanacimiento, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.estado, persona.direccion, persona.genero, medico.maxcitaspordia, " +
                "especialidad.codigo_especialidad, especialidad.nombre AS nombre_esp " +
                "from medico " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "inner join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                esp.setNombre(rs.getString("nombre_esp"));

                medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                if (rs.getDate("fechanacimiento") != null) {
                    medico.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                medico.setCedula(rs.getString("cedula"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setDireccion(rs.getString("direccion"));
                medico.setEstado(rs.getBoolean("estado"));
                medico.setGenero(rs.getString("genero"));
                medico.setMaxCitasPorDia(rs.getInt("maxcitaspordia"));
                medico.setEspecialidad(esp);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return medico;
    }

    public ArrayList<Medico> listarMedicos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.fechanacimiento, persona.nombre, persona.apellido, " +
                "persona.cedula, persona.telefono, persona.estado, persona.direccion, persona.genero, medico.maxcitaspordia, " +
                "especialidad.codigo_especialidad, especialidad.nombre AS nombre_esp " +
                "from medico " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "inner join especialidad on medico.codigo_especialidad = especialidad.codigo_especialidad";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                esp.setNombre(rs.getString("nombre_esp"));

                Medico medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                if (rs.getDate("fechanacimiento") != null) {
                    medico.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                medico.setCedula(rs.getString("cedula"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setDireccion(rs.getString("direccion"));
                medico.setEstado(rs.getBoolean("estado"));
                medico.setGenero(rs.getString("genero"));
                medico.setMaxCitasPorDia(rs.getInt("maxcitaspordia"));
                medico.setEspecialidad(esp);

                lista.add(medico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean verificarDisponibilidad(String cedula, LocalDateTime fechaHora, LocalDateTime finHora) {
        boolean disponible = false;
        String sql = "select count(cita.codigo_cita) AS solapamientos " +
                "from cita " +
                "inner join medico on cita.codigo_medico = medico.codigo_persona " +
                "inner join persona on medico.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ? and cita.estado = 'Pendiente' and (cita.fechacita < ? and dateadd(minute, 30, cita.fechacita) > ?)";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

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

    public ArrayList<Medico> listarMedicosActivos() {
        ArrayList<Medico> lista = new ArrayList<>();
        String sql = "select vw_directorio_medico.* from vw_directorio_medico";

        try (Connection conn = ConexionDB.getConexion(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setCodigoEspecialidad(rs.getInt("codigo_especialidad"));
                esp.setNombre(rs.getString("nombre_esp"));

                Medico medico = new Medico();
                medico.setCodigoPersona(rs.getInt("codigo_persona"));
                if (rs.getDate("fechanacimiento") != null) {
                    medico.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                medico.setNombre(rs.getString("nombre"));
                medico.setApellido(rs.getString("apellido"));
                medico.setCedula(rs.getString("cedula"));
                medico.setTelefono(rs.getString("telefono"));
                medico.setDireccion(rs.getString("direccion"));
                medico.setEstado(rs.getBoolean("estado"));
                medico.setGenero(rs.getString("genero"));
                medico.setMaxCitasPorDia(rs.getInt("maxcitaspordia"));
                medico.setEspecialidad(esp);

                lista.add(medico);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}