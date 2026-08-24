package Servicios;

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
        String sqlPersona = "insert into persona (fechanacimiento, nombre, apellido, cedula, telefono, estado, direccion, genero) " +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";

        String sqlEnf = "insert into enfermera (codigo_persona, turno) " +
                "values (?, ?)";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona, PreparedStatement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false);

            stmtPersona.setDate(1, Date.valueOf(enfermera.getFechaNacimiento()));
            stmtPersona.setString(2, enfermera.getNombre());
            stmtPersona.setString(3, enfermera.getApellido());
            stmtPersona.setString(4, enfermera.getCedula());
            stmtPersona.setString(5, enfermera.getTelefono());
            stmtPersona.setBoolean(6, enfermera.getEstado());
            stmtPersona.setString(7, enfermera.getDireccion());
            stmtPersona.setString(8, enfermera.getGenero());

            stmtPersona.executeUpdate();
            ResultSet rs = stmtPersona.getGeneratedKeys();
            int idPersona = 0;
            if (rs.next()) {
                idPersona = rs.getInt(1);
            }

            try (PreparedStatement stmtEnf = conn.prepareStatement(sqlEnf)) {
                stmtEnf.setInt(1, idPersona);
                stmtEnf.setString(2, enfermera.getTurno());
                stmtEnf.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editEnfermera(Enfermera enfermera) {
        String sqlPersona = "update persona set persona.fechanacimiento = ?, persona.nombre = ?, persona.apellido = ?, " +
                "persona.telefono = ?, persona.direccion = ?, persona.estado = ?, persona.genero = ? where persona.cedula = ?";

        String sqlEnf = "update enfermera set enfermera.turno = ? " +
                "where enfermera.codigo_persona = (" +
                "select persona.codigo_persona " +
                "from persona " +
                "where persona.cedula = ?)";

        try (Connection conn = ConexionDB.getConexion()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPersona = conn.prepareStatement(sqlPersona)) {
                stmtPersona.setDate(1, Date.valueOf(enfermera.getFechaNacimiento()));
                stmtPersona.setString(2, enfermera.getNombre());
                stmtPersona.setString(3, enfermera.getApellido());
                stmtPersona.setString(4, enfermera.getTelefono());
                stmtPersona.setString(5, enfermera.getDireccion());
                stmtPersona.setBoolean(6, enfermera.getEstado());
                stmtPersona.setString(7, enfermera.getGenero());
                stmtPersona.setString(8, enfermera.getCedula());
                stmtPersona.executeUpdate();
            }

            try (PreparedStatement stmtEnf = conn.prepareStatement(sqlEnf)) {
                stmtEnf.setString(1, enfermera.getTurno());
                stmtEnf.setString(2, enfermera.getCedula());
                stmtEnf.executeUpdate();
            }

            conn.commit();
            conn.setAutoCommit(true);
            return true;

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
        String sql = "update persona set persona.estado = 0 " +
                "where persona.cedula = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cedula);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}