package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class TipoAnalisisService {

    public boolean crearTipoAnalisis(TipoAnalisis tipo) {
        String sql = "insert into tipo_analisis (nombre, descripcion) values (?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.getNombre());
            stmt.setString(2, tipo.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editTipoAnalisis(TipoAnalisis tipo) {
        String sql = "update tipo_analisis set nombre = ?, descripcion = ? where codigo_tipo = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, tipo.getNombre());
            stmt.setString(2, tipo.getDescripcion());
            stmt.setInt(3, tipo.getCodigoTipo());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarTipoAnalisis(int codigoTipo) {
        String sql = "delete from tipo_analisis where codigo_tipo = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoTipo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public TipoAnalisis buscarTipoAnalisis(int codigoTipo) {
        TipoAnalisis tipo = null;
        String sql = "select codigo_tipo, nombre, descripcion from tipo_analisis where codigo_tipo = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoTipo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                tipo = new TipoAnalisis();
                tipo.setCodigoTipo(rs.getInt("codigo_tipo"));
                tipo.setNombre(rs.getString("nombre"));
                tipo.setDescripcion(rs.getString("descripcion"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tipo;
    }

    public ArrayList<TipoAnalisis> listarTiposAnalisis() {
        ArrayList<TipoAnalisis> lista = new ArrayList<>();
        String sql = "select codigo_tipo, nombre, descripcion from tipo_analisis";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                TipoAnalisis tipo = new TipoAnalisis();
                tipo.setCodigoTipo(rs.getInt("codigo_tipo"));
                tipo.setNombre(rs.getString("nombre"));
                tipo.setDescripcion(rs.getString("descripcion"));

                lista.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
