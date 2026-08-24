package Servicios;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;
import java.util.ArrayList;

import logico.*;
import Utils.ConexionDB;

public class EnfermeraService {

    public boolean crearEnfermera(Enfermera enfermera) {
        String sql = "{call sp_crear_enfermera(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(enfermera.getFechaNacimiento()));
            stmt.setString(2, enfermera.getNombre());
            stmt.setString(3, enfermera.getApellido());
            stmt.setString(4, enfermera.getCedula());
            stmt.setString(5, enfermera.getTelefono());
            stmt.setBoolean(6, enfermera.getEstado());
            stmt.setString(7, enfermera.getDireccion());
            stmt.setString(8, enfermera.getGenero());
            stmt.setString(9, enfermera.getTurno());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editEnfermera(Enfermera enfermera) {
        String sql = "{call sp_editar_enfermera(?, ?, ?, ?, ?, ?, ?, ?, ?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setDate(1, Date.valueOf(enfermera.getFechaNacimiento()));
            stmt.setString(2, enfermera.getNombre());
            stmt.setString(3, enfermera.getApellido());
            stmt.setString(4, enfermera.getTelefono());
            stmt.setString(5, enfermera.getDireccion());
            stmt.setBoolean(6, enfermera.getEstado());
            stmt.setString(7, enfermera.getGenero());
            stmt.setString(8, enfermera.getCedula());
            stmt.setString(9, enfermera.getTurno());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Enfermera buscarEnfermera(String cedula) {
        Enfermera enfermera = null;
        String sql = "select persona.codigo_persona, persona.cedula, persona.nombre, persona.apellido, " +
                "persona.fechanacimiento, persona.telefono, persona.direccion, persona.estado, " +
                "persona.genero, enfermera.turno " +
                "from enfermera " +
                "inner join persona on enfermera.codigo_persona = persona.codigo_persona " +
                "where persona.cedula = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                enfermera = new Enfermera();
                enfermera.setCodigoPersona(rs.getInt("codigo_persona"));
                enfermera.setCedula(rs.getString("cedula"));
                enfermera.setNombre(rs.getString("nombre"));
                enfermera.setApellido(rs.getString("apellido"));
                if (rs.getDate("fechanacimiento") != null) {
                    enfermera.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                enfermera.setTelefono(rs.getString("telefono"));
                enfermera.setDireccion(rs.getString("direccion"));
                enfermera.setEstado(rs.getBoolean("estado"));
                enfermera.setGenero(rs.getString("genero"));
                enfermera.setTurno(rs.getString("turno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enfermera;
    }

    public ArrayList<Enfermera> listarEnfermeras() {
        ArrayList<Enfermera> lista = new ArrayList<>();
        String sql = "select persona.codigo_persona, persona.cedula, persona.nombre, persona.apellido, persona.telefono, " +
                "persona.direccion, persona.genero, persona.estado, persona.fechanacimiento, enfermera.turno " +
                "from enfermera " +
                "inner join persona on enfermera.codigo_persona = persona.codigo_persona";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Enfermera enfermera = new Enfermera();
                enfermera.setCodigoPersona(rs.getInt("codigo_persona"));
                enfermera.setCedula(rs.getString("cedula"));
                enfermera.setNombre(rs.getString("nombre"));
                enfermera.setApellido(rs.getString("apellido"));
                enfermera.setTelefono(rs.getString("telefono"));
                enfermera.setDireccion(rs.getString("direccion"));
                enfermera.setGenero(rs.getString("genero"));
                enfermera.setEstado(rs.getBoolean("estado"));
                if (rs.getDate("fechanacimiento") != null) {
                    enfermera.setFechaNacimiento(rs.getDate("fechanacimiento").toLocalDate());
                }
                enfermera.setTurno(rs.getString("turno"));

                lista.add(enfermera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean desactivarEnfermera(String cedula) {
        String sql = "{call sp_desactivar_enfermera(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}