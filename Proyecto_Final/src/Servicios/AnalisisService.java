package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.util.ArrayList;

public class AnalisisService {

    public boolean crearAnalisis(Analisis analisis) {
        String sql = "{call sp_crear_analisis(?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, analisis.getConsulta() != null ? analisis.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, analisis.getTipo() != null ? analisis.getTipo().getCodigoTipo() : 0);
            stmt.setTimestamp(3, analisis.getFechaOrden() != null ? Timestamp.valueOf(analisis.getFechaOrden()) : null);
            stmt.setTimestamp(4, analisis.getFechaResultado() != null ? Timestamp.valueOf(analisis.getFechaResultado()) : null);
            stmt.setString(5, analisis.getEstado());
            stmt.setString(6, analisis.getResultado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editAnalisis(Analisis analisis) {
        String sql = "{call sp_editar_analisis(?, ?, ?, ?, ?, ?, ?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, analisis.getCodigoAnalisis());
            stmt.setInt(2, analisis.getConsulta() != null ? analisis.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(3, analisis.getTipo() != null ? analisis.getTipo().getCodigoTipo() : 0);
            stmt.setTimestamp(4, analisis.getFechaOrden() != null ? Timestamp.valueOf(analisis.getFechaOrden()) : null);
            stmt.setTimestamp(5, analisis.getFechaResultado() != null ? Timestamp.valueOf(analisis.getFechaResultado()) : null);
            stmt.setString(6, analisis.getEstado());
            stmt.setString(7, analisis.getResultado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAnalisis(int codigoAnalisis) {
        String sql = "{call sp_eliminar_analisis(?)}";
        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoAnalisis);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Analisis buscarAnalisis(int codigoAnalisis) {
        Analisis analisis = null;
        String sql = "{call sp_buscar_analisis(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setInt(1, codigoAnalisis);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fechaOrden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fechaOrden").toLocalDateTime());
                }
                if (rs.getTimestamp("fechaResultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fechaResultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return analisis;
    }

    public ArrayList<Analisis> listarAnalisis() {
        ArrayList<Analisis> lista = new ArrayList<>();
        String sql = "{call sp_listar_analisis()}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Analisis analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fechaOrden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fechaOrden").toLocalDateTime());
                }
                if (rs.getTimestamp("fechaResultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fechaResultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);

                lista.add(analisis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public ArrayList<Analisis> getAnalisisPorDoctor(String cedulaMedico) {
        ArrayList<Analisis> lista = new ArrayList<>();
        String sql = "{call sp_analisis_por_doctor(?)}";

        try (Connection conn = ConexionDB.getConexion();
             CallableStatement stmt = conn.prepareCall(sql)) {

            stmt.setString(1, cedulaMedico);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Analisis analisis = new Analisis();
                analisis.setCodigoAnalisis(rs.getInt("codigo_analisis"));

                if (rs.getTimestamp("fechaOrden") != null) {
                    analisis.setFechaOrden(rs.getTimestamp("fechaOrden").toLocalDateTime());
                }
                if (rs.getTimestamp("fechaResultado") != null) {
                    analisis.setFechaResultado(rs.getTimestamp("fechaResultado").toLocalDateTime());
                }

                analisis.setEstado(rs.getString("estado"));
                analisis.setResultado(rs.getString("resultado"));

                Consulta c = new Consulta();
                c.setCodigoConsulta(rs.getInt("codigo_cons"));
                analisis.setConsulta(c);

                TipoAnalisis t = new TipoAnalisis();
                t.setCodigoTipo(rs.getInt("codigo_tipo"));
                t.setNombre(rs.getString("tipo_nombre"));
                t.setDescripcion(rs.getString("tipo_desc"));
                analisis.setTipo(t);

                lista.add(analisis);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}