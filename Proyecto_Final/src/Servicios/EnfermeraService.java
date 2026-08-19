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
        String sql = "insert into enfermera (codigo_persona, turno) values (?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, enfermera.getCodigoPersona());
            stmt.setString(2, enfermera.getTurno());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editEnfermera(Enfermera enfermera) {
        String sql = "update enfermera set turno = ? where codigo_persona = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, enfermera.getTurno());
            stmt.setInt(2, enfermera.getCodigoPersona());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Enfermera buscarEnfermera(String cedula) {
        Enfermera enfermera = null;
        String sql = "select p.codigo_persona, p.cedula, p.nombre, p.apellido, e.turno " +
                "from enfermera e inner join persona p on e.codigo_persona = p.codigo_persona " +
                "where p.cedula = ?";

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
                enfermera.setTurno(rs.getString("turno"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return enfermera;
    }

    public ArrayList<Enfermera> listarEnfermeras() {
        ArrayList<Enfermera> lista = new ArrayList<>();
        String sql = "select p.codigo_persona, p.cedula, p.nombre, p.apellido, p.telefono, p.direccion, p.genero, p.estado, e.turno " +
                "from enfermera e inner join persona p on e.codigo_persona = p.codigo_persona";

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
                enfermera.setTurno(rs.getString("turno"));

                lista.add(enfermera);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean desactivarEnfermera(String cedula) {
        String sql = "UPDATE persona SET estado = false WHERE cedula = ?";
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