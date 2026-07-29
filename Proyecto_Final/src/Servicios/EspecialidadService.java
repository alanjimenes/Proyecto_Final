package Servicios;

import Utils.ConexionDB;
import logico.Especialidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EspecialidadService {

    public boolean registrarEspecialidad(Especialidad esp) {
        String sql = "insert into especialidad (nombre) values " +
                "(?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, esp.getNombre());
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Especialidad> listarEspecialidades() {
        ArrayList<Especialidad> lista = new ArrayList<>();
        String sql = "select especialidad.codigo_especialidad, especialidad.nombre " +
                "from especialidad";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                lista.add(new Especialidad(
                        String.valueOf(rs.getInt("codigo_especialidad")),
                        rs.getString("nombre")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Especialidad buscarEspecialidadPorNombre(String nombre) {
        Especialidad especialidad = null;
        String sql = "select especialidad.codigo_especialidad, especialidad.nombre " +
                "from especialidad " +
                "where especialidad.nombre = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                especialidad = new Especialidad(
                        String.valueOf(rs.getInt("codigo_especialidad")),
                        rs.getString("nombre")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return especialidad;
    }

    public boolean actualizarEspecialidad(Especialidad esp) {
        String sql = "update especialidad set especialidad.nombre = ? " +
                "where especialidad.codigo_especialidad = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, esp.getNombre());
            stmt.setInt(2, Integer.parseInt(esp.getCodigo_espe()));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarEspecialidad(String codigo) {
        String sql = "delete from especialidad " +
                "where especialidad.codigo_especialidad = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, Integer.parseInt(codigo));
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}