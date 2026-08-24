package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class TipoAnalisisService {

    public boolean crearTipoAnalisis(TipoAnalisis tipo) {
        String sql = "{call sp_crear_tipo_analisis(?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, tipo.getNombre());
            stmt.setString(2, tipo.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editTipoAnalisis(TipoAnalisis tipo) {
        String sql = "{call sp_editar_tipo_analisis(?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, tipo.getCodigoTipo());
            stmt.setString(2, tipo.getNombre());
            stmt.setString(3, tipo.getDescripcion());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarTipoAnalisis(int codigoTipo) {
        String sql = "{call sp_eliminar_tipo_analisis(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoTipo);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public TipoAnalisis buscarTipoAnalisis(int codigoTipo) {
        TipoAnalisis tipo = null;
        String sql = "select tipo_analisis.codigo_tipo, tipo_analisis.nombre, tipo_analisis.descripcion " +
                "from tipo_analisis " +
                "where tipo_analisis.codigo_tipo = ?";

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
        String sql = "select tipo_analisis.codigo_tipo, tipo_analisis.nombre, tipo_analisis.descripcion " +
                "from tipo_analisis";

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