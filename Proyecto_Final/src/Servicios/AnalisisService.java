package Servicios;

import Utils.ConexionDB;
import logico.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AnalisisService {

    public boolean crearAnalisis(Analisis analisis) {
        String sql = "insert into analisis (codigo_cons, codigo_tipo, fechaOrden, fechaResultado, estado, resultado) " +
                "values (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "update analisis set codigo_cons = ?, codigo_tipo = ?, fechaOrden = ?, fechaResultado = ?, " +
                "estado = ?, resultado = ? " +
                "WHERE codigo_analisis = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, analisis.getConsulta() != null ? analisis.getConsulta().getCodigoConsulta() : 0);
            stmt.setInt(2, analisis.getTipo() != null ? analisis.getTipo().getCodigoTipo() : 0);
            stmt.setTimestamp(3, analisis.getFechaOrden() != null ? Timestamp.valueOf(analisis.getFechaOrden()) : null);
            stmt.setTimestamp(4, analisis.getFechaResultado() != null ? Timestamp.valueOf(analisis.getFechaResultado()) : null);
            stmt.setString(5, analisis.getEstado());
            stmt.setString(6, analisis.getResultado());
            stmt.setInt(7, analisis.getCodigoAnalisis());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean eliminarAnalisis(int codigoAnalisis) {
        String sql = "delete from analisis " +
                "where codigo_analisis = ?";
        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigoAnalisis);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Analisis buscarAnalisis(int codigoAnalisis) {
        Analisis analisis = null;
        String sql = "select analisis.codigo_analisis, analisis.codigo_cons, analisis.fechaOrden, analisis.fechaResultado, " +
                "analisis.estado, analisis.resultado, " +
                "tipo_analisis.codigo_tipo, tipo_analisis.nombre as tipo_nombre, tipo_analisis.descripcion as tipo_desc " +
                "from analisis  " +
                "left join tipo_analisis ON analisis.codigo_tipo = tipo_analisis.codigo_tipo " +
                "where analisis.codigo_analisis = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
        String sql = "select analisis.codigo_analisis, analisis.codigo_cons, analisis.fechaOrden, analisis.fechaResultado, " +
                "analisis.estado, analisis.resultado, " +
                "tipo_analisis.codigo_tipo, tipo_analisis.nombre AS tipo_nombre, tipo_analisis.descripcion AS tipo_desc " +
                "from analisis " +
                "left join tipo_analisis on analisis.codigo_tipo = tipo_analisis.codigo_tipo";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
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
        String sql = "SELECT analisis.codigo_analisis, analisis.codigo_cons, analisis.fechaOrden, analisis.fechaResultado, " +
                "analisis.estado, analisis.resultado, " +
                "tipo_analisis.codigo_tipo, tipo_analisis.nombre AS tipo_nombre, tipo_analisis.descripcion AS tipo_desc " +
                "FROM analisis " +
                "LEFT JOIN tipo_analisis ON analisis.codigo_tipo = tipo_analisis.codigo_tipo " +
                "INNER JOIN consulta ON analisis.codigo_cons = consulta.codigo_consulta " +
                "WHERE consulta.cedula_medico = ?";

        try (Connection conn = ConexionDB.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

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
